package theweeb.dev.chatbotbiometricauth.components

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import theweeb.dev.chatbotbiometricauth.R
import java.util.Locale

@Composable
fun MessageField(
    modifier: Modifier = Modifier,
    message: String,
    isConversationEmpty: Boolean,
    suggestedMessage: String,
    isSendingMessage: Boolean,
    onValueChange: (String) -> Unit,
    sendMessage: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            result?.let {
                sendMessage(it[0])
            }
        }
    }
    TextField(
        modifier = modifier
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