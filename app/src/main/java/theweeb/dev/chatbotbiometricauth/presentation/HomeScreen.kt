package theweeb.dev.chatbotbiometricauth.presentation

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import theweeb.dev.chatbotbiometricauth.components.ChatBubble
import theweeb.dev.chatbotbiometricauth.components.ClearMessageDialog
import theweeb.dev.chatbotbiometricauth.components.LatestNavigator
import theweeb.dev.chatbotbiometricauth.components.MessageField
import theweeb.dev.chatbotbiometricauth.components.ModelSnackBar
import theweeb.dev.chatbotbiometricauth.components.PersonalityPicker
import theweeb.dev.chatbotbiometricauth.model.ModelPersonality
import java.io.InputStream

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    conversationState: ConversationState,
    drawerState: DrawerState,
    viewModel: AppViewModel,
    openDrawer: () -> Unit
){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getModelPersonality(context)
    }

    BackHandler(
        enabled = conversationState.model != null
    ) {
        if(drawerState.isOpen)
            scope.launch { drawerState.close() }
        else
            viewModel.clearModel(context)
    }

    HomeScreen(
        modifier = modifier,
        storedImageBitmap = conversationState.bitmap,
        conversationState = conversationState,
        conversationEvent = viewModel::conversationEvent,
        saveModelPersonality = viewModel::saveModelPersonality,
        openDrawer = openDrawer,
        clearModel = viewModel::clearModel,
        clearSnackBarMessage = viewModel::clearSnackBarMessage,
        getImage = viewModel::getBitMapFromUri,
        clearImage = viewModel::clearBitMap,
        onMessageChange = viewModel::onMessageChange
    )
}

@OptIn(ExperimentalMaterial3Api::class
)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    storedImageBitmap: Bitmap?,
    conversationState: ConversationState,
    conversationEvent: (ConversationEvent) -> Unit,
    saveModelPersonality: (Context, ModelPersonality) -> Unit,
    openDrawer: () -> Unit,
    clearModel: (Context) -> Unit,
    clearSnackBarMessage: () -> Unit,
    getImage: (Bitmap?) -> Unit,
    clearImage: () -> Unit,
    onMessageChange: (String) -> Unit
) {

    val lazyListState = rememberLazyListState()

    val isLastItemVisible by remember {
        derivedStateOf {
            val item = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull {
                it.index == 0
            }
            if(item != null) return@derivedStateOf true else false
        }
    }


    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isClearDialogOpen by remember {
        mutableStateOf(false)
    }

    val snackBarState = remember {
        SnackbarHostState()
    }

    if(isClearDialogOpen)
        ClearMessageDialog(
            title = "Clear messages?",
            dismiss = { isClearDialogOpen = !isClearDialogOpen },
            confirm = {
                conversationEvent(ConversationEvent.ResetConversation)
            }
        )

    DisposableEffect(conversationState.modelNoteResponse) {
        if(conversationState.modelNoteResponse.isNotBlank())
            scope.launch { snackBarState.showSnackbar("") }

        onDispose {
            clearSnackBarMessage()
        }
    }

    LaunchedEffect(conversationState.conversationMessages) {
        if(conversationState.conversationMessages.isNotEmpty())
            lazyListState.animateScrollToItem(0)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = conversationState.getScaffoldTitle(), overflow = TextOverflow.Ellipsis, maxLines = 1)
                },
                navigationIcon = {
                    if(conversationState.model != null)
                        IconButton(onClick = { clearModel(context) }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    else
                        IconButton(onClick = openDrawer) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                },
                actions = {
                    if(conversationState.model != null && conversationState.conversationMessages.isNotEmpty())
                        IconButton(onClick = { isClearDialogOpen = true} ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        }
                },
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Box(modifier = modifier.padding(paddingValues)) {
            Column(
                modifier = modifier
            ) {
                Box(modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()) {
                    if(conversationState.isConversationLoading)
                        CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
                    else
                        AnimatedContent(
                            targetState = conversationState.model == null,
                            transitionSpec = {
                                scaleIn() togetherWith scaleOut()
                            },
                            label = "",
                        ) {
                            when(it){
                                true ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        PersonalityPicker(modifier = modifier.fillMaxWidth()){ personality ->
                                            saveModelPersonality(context, personality)
                                        }
                                    }
                                false ->
                                    LazyColumn(
                                        state = lazyListState,
                                        modifier = modifier,
                                        contentPadding = PaddingValues(16.dp),
                                        reverseLayout = true,
                                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
                                    ){
                                        item {
                                            Spacer(
                                                modifier
                                                    .windowInsetsBottomHeight(
                                                        WindowInsets.systemBars
                                                    )
                                            )
                                        }
                                        if(conversationState.conversationMessages.isEmpty())
                                            item {
                                                Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "Chat empty, please send message to the Chatbot.",
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        else
                                            items(
                                                conversationState.conversationMessages.reversed(),
                                                key = { message -> message.messageId}
                                            ){message ->
                                                ChatBubble(
                                                    message = message,
                                                    modelImage = conversationState.model?.personality?.image ?: 0
                                                )
                                            }
                                    }
                            }
                        }
                    LatestNavigator(
                        modifier = modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp),
                        isVisible = !isLastItemVisible && conversationState.conversationMessages.isNotEmpty()
                    ){
                        scope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    }
                    SnackbarHost(
                        modifier = modifier.align(Alignment.BottomCenter),
                        hostState = snackBarState
                    ){
                        ModelSnackBar(
                            modelImage = conversationState.model?.personality?.image ?: 0,
                            modelResponse = conversationState.modelNoteResponse.ifBlank { "I have created a note for you." }
                        )
                    }
                }
                AnimatedVisibility(visible = conversationState.model != null) {
                    MessageField(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp)
                            .imePadding(),
                        message = conversationState.currentMessage,
                        contextResolver = context.contentResolver,
                        storedImageBitmap = storedImageBitmap,
                        isConversationEmpty = conversationState.conversationMessages.isEmpty(),
                        suggestedMessage = conversationState.model?.personality?.suggestedConversation ?: "",
                        isSendingMessage = conversationState.messageCollectionJob?.isActive == true,
                        onValueChange = onMessageChange,
                        getImage = getImage,
                        clearImage = clearImage,
                        sendMessage = {
                            conversationEvent(ConversationEvent.SendTextMessage(it))
                        }
                    )
                }
            }
        }
    }
}