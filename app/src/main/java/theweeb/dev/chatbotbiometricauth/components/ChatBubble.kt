package theweeb.dev.chatbotbiometricauth.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import theweeb.dev.chatbotbiometricauth.model.Message
import theweeb.dev.chatbotbiometricauth.model.ResponseType

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    message: Message,
    modelImage: Int,
    shape: RoundedCornerShape = RoundedCornerShape(
        topStart = 100f,
        topEnd = 0f,
        bottomStart = 100f,
        bottomEnd = 100f
    )) {

    var isUserMessageDetailShown by remember {
        mutableStateOf(false)
    }

    var isModelMessageDetailShown by remember {
        mutableStateOf(false)
    }

    when(message.responseType){
        ResponseType.USER.name -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 100.dp, bottom = 1.dp)
                    .clickable { isUserMessageDetailShown = !isUserMessageDetailShown },
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                ElevatedCard(
                    modifier = modifier,
                    shape = shape
                ) {
                    Text(
                        text = AnnotatedString(text = message.content),
                        modifier = modifier.padding(12.dp)
                    )
                    if(message.imageData != null){
                        val bitmap = byteArrayToBitmap(message.imageData)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                AnimatedVisibility(visible = isUserMessageDetailShown) {
                    Text(
                        text = message.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
        ResponseType.MODEL.name -> {
            Row(
                modifier = modifier
                    .padding(end = 20.dp)
                    .clickable { isModelMessageDetailShown = !isModelMessageDetailShown },
                verticalAlignment = Alignment.Top
            ) {
                if(modelImage != 0){
                    Image(
                        painter = painterResource(id = modelImage),
                        contentDescription = null,
                        modifier = modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = modifier.width(12.dp))
                Column(modifier = modifier.weight(1f)) {
                    Text(
                        text = message.content
                    )
                    AnimatedVisibility(visible = isModelMessageDetailShown) {
                        Text(
                            text = message.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
        ResponseType.ERROR.name -> {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.errorContainer)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.error)
                    .clickable { isModelMessageDetailShown = !isModelMessageDetailShown }
            ) {
                Text(
                    text = message.content,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                AnimatedVisibility(visible = isModelMessageDetailShown) {
                    Text(
                        text = message.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}