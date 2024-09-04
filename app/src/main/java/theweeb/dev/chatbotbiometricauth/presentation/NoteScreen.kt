package theweeb.dev.chatbotbiometricauth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import theweeb.dev.chatbotbiometricauth.components.NoteField
import theweeb.dev.chatbotbiometricauth.model.Note

@Composable
fun NoteRoute(
    modifier: Modifier = Modifier,
    noteId: String,
    viewModel: AppViewModel,
    back: () -> Unit
) {

    val noteState by viewModel.noteState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getNote(noteId)
    }

    NoteScreen(
        modifier = modifier
            .navigationBarsPadding(),
        noteState = noteState,
        noteEvent = viewModel::noteEvent,
        onNoteTitleChange = viewModel::onNoteTitleChange,
        onNoteContentChange = viewModel::onNoteContentChange,
        back = back
    )
}

@Composable
private fun NoteScreen(
    modifier: Modifier = Modifier,
    noteState: NoteState,
    noteEvent: (NoteEvent) -> Unit,
    onNoteTitleChange: (String) -> Unit,
    onNoteContentChange: (String) -> Unit,
    back: () -> Unit,
) {

    DisposableEffect(Unit) {
        onDispose {
            noteEvent(NoteEvent.UpsertNote(noteState.note))
        }
    }

    NoteScreen(
        modifier = modifier,
        state = noteState,
        onNoteTitleChange = onNoteTitleChange,
        onNoteContentChange = onNoteContentChange,
        upsert = { noteEvent(NoteEvent.UpsertNote(noteState.note)) },
        back = back
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteScreen(
    modifier: Modifier = Modifier,
    state: NoteState,
    onNoteTitleChange: (String) -> Unit,
    onNoteContentChange: (String) -> Unit,
    upsert: () -> Unit,
    back: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if(isFocused)
                        IconButton(
                            onClick = {
                                upsert()
                                focusManager.clearFocus()
                                isFocused = false
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = null)
                        }
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Box(
            modifier = modifier.padding(paddingValues)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                NoteField(
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester1),
                    value = state.note.title,
                    placeholder = "Title",
                    style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    onValueChange = {
                        focusRequester1.requestFocus()
                        isFocused = true
                        onNoteTitleChange(it)
                    }
                )
                NoteField(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .focusRequester(focusRequester2),
                    value = state.note.content,
                    placeholder = "Content",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    onValueChange = {
                        focusRequester2.requestFocus()
                        isFocused = true
                        onNoteContentChange(it)
                    }
                )
            }
        }
    }
}