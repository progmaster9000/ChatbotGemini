package theweeb.dev.chatbotbiometricauth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import theweeb.dev.chatbotbiometricauth.model.ModelPersonality
import theweeb.dev.chatbotbiometricauth.model.ModelPersonalityItem

@Composable
fun PersonalityPicker(
    modifier: Modifier = Modifier,
    personalities: List<ModelPersonalityItem> = ModelPersonalityItem.getPersonalities(),
    onChosenPersonality: (ModelPersonality) -> Unit
) {
    Box(modifier = modifier.padding(16.dp)) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = personalities
            ){
                ElevatedCard(
                    modifier = modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 10.dp
                    ),
                    onClick = {
                        onChosenPersonality(it.modelPersonality)
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            painter = painterResource(id = it.image),
                            contentDescription = null,
                            modifier = modifier
                                .height(150.dp)
                                .drawWithCache {
                                    val gradient = Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black),
                                        startY = size.height / 2,
                                        endY = size.height
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient, blendMode = BlendMode.Multiply)
                                    }
                                },
                            contentScale = ContentScale.FillWidth,
                        )
                        Text(
                            text = it.modelName,
                            modifier = modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
            item {
                AddModelButton(
                    onAddClick = {}
                )
            }
        }
    }
}