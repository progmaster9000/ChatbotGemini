package theweeb.dev.chatbotbiometricauth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import theweeb.dev.chatbotbiometricauth.model.NoteTuple
import theweeb.dev.chatbotbiometricauth.model.ResponseType
import java.util.Locale

@Composable
fun NoteContainer(
    modifier: Modifier = Modifier,
    isCheckBoxVisible: Boolean = false,
    note: NoteTuple,
    onLongPress: () -> Unit,
    onCheck: (String?) -> Unit,
    onNoteClick: (String) -> Unit,
) {

    var isChecked by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val viewConfiguration = LocalViewConfiguration.current

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    delay(viewConfiguration.longPressTimeoutMillis)
                    onLongPress()
                }
                is PressInteraction.Release -> Unit
            }
        }
    }

    LaunchedEffect(isCheckBoxVisible){
        if(!isCheckBoxVisible)
            isChecked = false
    }

    LaunchedEffect(isChecked) {
        if(isChecked)
            onCheck(note.noteId)
        else
            onCheck(null)
    }

    ElevatedCard(
        modifier = modifier,
        onClick = {
            if(!isCheckBoxVisible){
                onNoteClick(note.noteId)
            }else{
                isChecked = !isChecked
            }
        },
        interactionSource = interactionSource
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(text = note.title.ifEmpty { "Empty Title" }, style = MaterialTheme.typography.headlineSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = note.date, color = MaterialTheme.colorScheme.outlineVariant, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = if(!isCheckBoxVisible) buildAnnotatedString {
                        append("created by: ")
                        withStyle(SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium
                        )){
                            append(note.responseType.lowercase(Locale.ROOT))
                        }
                    } else AnnotatedString(""),
                    modifier = modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.bodySmall
                )
            }
            AnimatedVisibility(
                visible = isCheckBoxVisible,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ),
                exit = ExitTransition.None
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = !isChecked
                    }
                )
            }
        }
    }
}