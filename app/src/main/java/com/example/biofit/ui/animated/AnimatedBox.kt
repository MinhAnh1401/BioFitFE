package com.example.biofit.ui.animated

import android.content.res.Configuration
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

@Composable
fun BlinkingGradientBox(
    borderAlpha: Float,
    alpha: Float,
    shape: Shape,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()

    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFFAEEA00),
        targetValue = Color(0xFF2962FF),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF2962FF),
        targetValue = Color(0xFFAEEA00),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .clip(shape = shape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha),
                shape = shape
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(color1, color2),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .background(MaterialTheme.colorScheme.background.copy(alpha = alpha))
    ) {
        content()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
private fun AnimatedBoxDarkModePreview() {
    BioFitTheme {
        BlinkingGradientBox(
            borderAlpha = 0.25f,
            alpha = 0.5f,
            shape = MaterialTheme.shapes.extraLarge,
            content = {
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.background
                )
            }
        )
    }
}

@Preview
@Composable
private fun AnimatedBoxPreview() {
    BioFitTheme {
        BlinkingGradientBox(
            borderAlpha = 0.25f,
            alpha = 0.5f,
            shape = MaterialTheme.shapes.extraLarge,
            content = {
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.padding(getStandardPadding().first),
                    color = MaterialTheme.colorScheme.background
                )
            }
        )
    }
}