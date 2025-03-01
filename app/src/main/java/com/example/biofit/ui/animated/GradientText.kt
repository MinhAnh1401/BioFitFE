package com.example.biofit.ui.animated

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

@Composable
fun AnimatedGradientText(
    highlightColor: Color,
    textBodyColor1: Color,
    textBodyColor2: Color,
    text: String,
    style: TextStyle
) {
    val infiniteTransition = rememberInfiniteTransition()

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            textBodyColor1,
            highlightColor,
            textBodyColor2
        ),
        start = Offset(offsetX, offsetX),
        end = Offset(offsetX + 200f, offsetX + 200f)
    )

    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = style.copy(brush = gradientBrush)
    )
}

@Composable
fun OneTimeAnimatedGradientText(
    highlightColor: Color,
    baseColor: Color,
    hideColor: Color,
    text: String,
    style: TextStyle,
    onAnimationEnd: () -> Unit = {}
) {
    var textHeight by remember { mutableFloatStateOf(0f) }

    val transition = remember { Animatable(-300f) }

    LaunchedEffect(textHeight) {
        if (textHeight > 0f) {
            val startValue = -300f
            val endValue = textHeight
            val distance = endValue - startValue

            val speed = 0.5f
            val durationMillis = (distance / speed).toInt()

            transition.snapTo(startValue)
            transition.animateTo(
                targetValue = endValue,
                animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
            )
            onAnimationEnd()
        }
    }

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            hideColor
        ),
        start = Offset(0f, transition.value),
        end = Offset(0f, transition.value + 500f)
    )

    Text(
        text = text,
        style = style.copy(brush = gradientBrush),
        modifier = Modifier.onGloballyPositioned { coordinates ->
            textHeight = coordinates.size.height.toFloat()
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
private fun GradientTextDarkModePreview() {
    BioFitTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedGradientText(
                highlightColor = Color(0xFFAEEA00),
                textBodyColor1 = Color(0xFFAEEA00),
                textBodyColor2 = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.congratulations_you_have_completed),
                style = MaterialTheme.typography.displaySmall
            )

            var isAnimationFinished by remember { mutableStateOf(false) }

            OneTimeAnimatedGradientText(
                highlightColor = Color(0xFFAEEA00),
                baseColor = MaterialTheme.colorScheme.primary,
                hideColor = Color.Transparent,
                text = stringResource(R.string.congratulations_you_have_completed),
                style = MaterialTheme.typography.displaySmall,
                onAnimationEnd = { isAnimationFinished = true }
            )


        }
    }
}

@Preview
@Composable
private fun GradientTextPreview() {
    BioFitTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedGradientText(
                highlightColor = Color(0xFFAEEA00),
                textBodyColor1 = Color(0xFFAEEA00),
                textBodyColor2 = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.congratulations_you_have_completed),
                style = MaterialTheme.typography.displaySmall
            )

            var isAnimationFinished by remember { mutableStateOf(false) }

            OneTimeAnimatedGradientText(
                highlightColor = Color(0xFFAEEA00),
                baseColor = MaterialTheme.colorScheme.primary,
                hideColor = Color.Transparent,
                text = stringResource(R.string.congratulations_you_have_completed),
                style = MaterialTheme.typography.displaySmall,
                onAnimationEnd = { isAnimationFinished = true }
            )
        }
    }
}