package theweeb.dev.chatbotbiometricauth.components

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import theweeb.dev.chatbotbiometricauth.R
import java.io.InputStream
import java.util.Locale

@Composable
fun MessageField(
    modifier: Modifier = Modifier,
    storedImageBitmap: Bitmap?,
    message: String,
    isConversationEmpty: Boolean,
    suggestedMessage: String,
    isSendingMessage: Boolean,
    onValueChange: (String) -> Unit,
    getImage: (Bitmap?) -> Unit,
    clearImage: () -> Unit,
    sendMessage: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val contextResolver = LocalContext.current.contentResolver
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                getImage(uriToBitmap(contextResolver, it))
            }
        }
    )
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            result?.let {
                sendMessage(it[0])
            }
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        IconButton(
            onClick = { imageLauncher.launch("image/*") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.image), contentDescription = null)
        }
        TextField(
            modifier = modifier
                .weight(1f)
                .focusRequester(focusRequester),
            value = message,
            onValueChange = onValueChange,
            leadingIcon = {
                IconButton(onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, suggestedMessage)
                    launcher.launch(intent)
                }) {
                    Icon(painterResource(id = R.drawable.baseline_mic_24), contentDescription = null)
                }
            },
            trailingIcon = {
                if(isSendingMessage)
                    CircularProgressIndicator(
                        modifier = Modifier.then(Modifier.size(28.dp)),
                        strokeWidth = 3.dp,
                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
                    )
                else
                    if(message.isNotBlank())
                        IconButton(onClick = {
                            sendMessage(message)
                            focusManager.clearFocus()
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                        }
            },
            placeholder = {
                Text(text = if(isConversationEmpty) suggestedMessage else "chat here", color = MaterialTheme.colorScheme.outline)
            },
            supportingText = {
                if(storedImageBitmap != null && !isSendingMessage){
                    Row(
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Image(
                            bitmap = storedImageBitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = clearImage,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.then(
                                    Modifier.size(20.dp)
                                )
                            )
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences
            ),
            shape = RoundedCornerShape(100f),
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

private fun uriToBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun isValidImageFormat(uri: Uri): Boolean {
    val mimeType = uri.toString().substringAfterLast('.')
    return mimeType.equals("png", true) || mimeType.equals("jpg", true) || mimeType.equals("jpeg", true)
}