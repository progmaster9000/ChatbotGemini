package theweeb.dev.chatbotbiometricauth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun LatestNavigator(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    navigate: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
        visible = isVisible
    ) {
        FilledTonalIconButton(
            modifier = modifier.then(modifier.size(54.dp).shadow(elevation = 3.dp, shape = IconButtonDefaults.outlinedShape, clip = true)),
            onClick = navigate,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null,
            )
        }
    }
}