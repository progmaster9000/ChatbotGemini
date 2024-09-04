package theweeb.dev.chatbotbiometricauth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddModelButton(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    Box(modifier = modifier.padding(8.dp)) {
        FilledTonalIconButton(
            onClick = onAddClick
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}