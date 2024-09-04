package theweeb.dev.chatbotbiometricauth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ModelSnackBar(
    modifier: Modifier = Modifier,
    modelImage: Int,
    modelResponse: String
) {
    Snackbar(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = modelImage),
                contentDescription = null,
                modifier = modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = modifier.width(16.dp))
            Column {
                Text(text = modelResponse)
            }
        }
    }
}