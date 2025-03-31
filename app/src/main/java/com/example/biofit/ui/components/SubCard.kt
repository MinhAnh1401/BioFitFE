package com.example.biofit.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SubCard(
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    ElevatedCard(
        onClick = {
            coroutineScope.launch {
                onClick()
                scale.animateTo(0.99f, animationSpec = tween(100))
                delay(100)
                scale.animateTo(1f, animationSpec = tween(100))
            }
        },
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        content()
    }
}

@Composable
fun SubCard(
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        content()
    }
}

@Composable
fun ItemCard(
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    ElevatedCard(
        onClick = {
            coroutineScope.launch {
                onClick()
                scale.animateTo(0.99f, animationSpec = tween(100))
                delay(100)
                scale.animateTo(1f, animationSpec = tween(100))
            }
        },
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        content()
    }
}

@Composable
fun ItemCard(
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        content()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
    showBackground = true
)
@Composable
private fun PrimaryCardDarkModePreview() {
    BioFitTheme {
        Column {
            SubCard(
                onClick = {},
                modifier = Modifier.padding(getStandardPadding().first)
            ) {
                Text(
                    text = stringResource(R.string.welcome_to_app),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            SubCard(modifier = Modifier.padding(getStandardPadding().first)) {
                Text(
                    text = stringResource(R.string.welcome_to_app),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            ItemCard(
                onClick = {},
                modifier = Modifier.padding(getStandardPadding().first)
            ) {
                Text(
                    text = stringResource(R.string.welcome_to_app),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryCardLightModePreview() {
    BioFitTheme {
        Column {
            SubCard(
                onClick = {},
                modifier = Modifier.padding(getStandardPadding().first)
            ) {
                Text(
                    text = stringResource(R.string.welcome_to_app),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            SubCard(modifier = Modifier.padding(getStandardPadding().first)) {
                Text(
                    text = stringResource(R.string.welcome_to_app),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            ItemCard(
                onClick = {},
                modifier = Modifier.padding(getStandardPadding().first)
            ) {
                Text(
                    text = stringResource(R.string.welcome_to_app),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}