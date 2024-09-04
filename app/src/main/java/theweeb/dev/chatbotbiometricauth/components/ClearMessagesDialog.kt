package theweeb.dev.chatbotbiometricauth.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ClearMessageDialog(
    modifier: Modifier = Modifier,
    title: String,
    dismiss: () -> Unit,
    confirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = dismiss,
        confirmButton = {
            Button(
                onClick = {
                    confirm()
                    dismiss()
                }
            ){
                Text(text = "confirm")
            }
        },
        title = { Text(text = title)},
        text = { Text(text = "This action cannot be undone.")}
    )
}