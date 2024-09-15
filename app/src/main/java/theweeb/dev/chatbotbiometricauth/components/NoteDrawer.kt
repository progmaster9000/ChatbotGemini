package theweeb.dev.chatbotbiometricauth.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theweeb.dev.chatbotbiometricauth.model.NoteTuple
import theweeb.dev.chatbotbiometricauth.presentation.NoteEvent
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    notes: List<NoteTuple>,
    noteEvent: (NoteEvent) -> Unit,
    onNoteClick: (String) -> Unit,
    onAddNote: (String) -> Unit,
    content: @Composable () -> Unit
) {

    var isCheckBoxVisible by remember {
        mutableStateOf(false)
    }

    var isDialogOpened by remember {
        mutableStateOf(false)
    }

    val notesTobeDeleted = remember {
        mutableStateListOf<String>()
    }

    if(isDialogOpened)
        ClearMessageDialog(
            title = "Delete notes?",
            dismiss = { isDialogOpened = false },
            confirm = {
                noteEvent(NoteEvent.DeleteNotes(notesTobeDeleted))
                isCheckBoxVisible = false
            }
        )

    BackHandler(enabled = isCheckBoxVisible) {
        isCheckBoxVisible = false
    }

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            isCheckBoxVisible = false
        }
    }

    LaunchedEffect(isCheckBoxVisible) {
        notesTobeDeleted.clear()
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet{
                Row(
                    modifier = modifier.fillMaxWidth(.8f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(isCheckBoxVisible && notes.isNotEmpty())
                        IconButton(onClick = { isCheckBoxVisible = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    Text(if(isCheckBoxVisible && notes.isNotEmpty())"${notesTobeDeleted.size} selected" else "Notes", modifier = Modifier.padding(16.dp))
                    if(isCheckBoxVisible && notesTobeDeleted.isNotEmpty())
                        IconButton(
                            onClick = {
                                isDialogOpened = true
                            }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    else
                        IconButton(
                            onClick = {
                                onAddNote(UUID.randomUUID().toString())
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                }
                if(notes.isEmpty())
                    Box(modifier = modifier
                        .weight(1f)
                        .fillMaxWidth(.8f), contentAlignment = Alignment.Center) {
                        Text(text = "Empty notes, please add.")
                    }
                else
                    LazyColumn(
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth(.8f),
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(
                            items = notes,
                            key = { note -> note.noteId}
                        ){
                            Box(modifier = modifier.animateItemPlacement()) {
                                NoteContainer(
                                    modifier = modifier
                                        .padding(horizontal = 16.dp),
                                    note = it,
                                    isCheckBoxVisible = isCheckBoxVisible,
                                    onNoteClick = onNoteClick,
                                    onLongPress = {
                                        isCheckBoxVisible = true
                                    },
                                    onCheck = { id ->
                                        val noteId = notes.find { it.noteId == id }?.noteId
                                        val note = notesTobeDeleted.find { it != noteId }
                                        if(noteId != null){
                                            notesTobeDeleted.add(noteId)
                                        }else
                                            notesTobeDeleted.remove(note)
                                    }
                                )
                            }
                        }
                        item { Spacer(modifier = modifier.height(8.dp)) }
                    }
            }
        },
        drawerState = drawerState,
        content = content
    )
}