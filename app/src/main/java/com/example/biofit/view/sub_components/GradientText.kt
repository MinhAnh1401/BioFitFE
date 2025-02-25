package com.example.biofit.view.sub_components

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
import com.example.biofit.view.ui_theme.BioFitTheme

@Composable
fun AnimatedGradientText(
    repeatMode: RepeatMode,
    highlightColor: Color,
    baseColor: Color,
    text: String,
    style: TextStyle
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Giá trị offset dịch chuyển màu highlight
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -300f, // Xuất hiện từ ngoài màn hình
        targetValue = 1000f, // Di chuyển sang phải
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = repeatMode // Restart để lặp từ đầu
        )
    )

    // Gradient mô phỏng hiệu ứng LED đuổi
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            baseColor, // Màu nền
            highlightColor, // Màu chạy
            baseColor // Màu nền
        ),
        start = Offset(offsetX, offsetX),
        end = Offset(offsetX + 200f, offsetX + 200f) // Tăng khoảng cách để hiệu ứng mượt hơn
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
    // Lưu chiều cao của text khi được đo
    var textHeight by remember { mutableStateOf(0f) }

    // Biến Animatable để điều khiển vị trí của gradient theo chiều dọc.
    val transition = remember { Animatable(-300f) }

    LaunchedEffect(textHeight) {
        if (textHeight > 0f) {
            // Gradient chạy từ trên (ngoài view) đến dưới (ngoài view)
            val startValue = -300f
            val endValue = textHeight
            val distance = endValue - startValue

            // Giả sử tốc độ LED là 0.5 pixel/ms
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

    // Gradient brush di chuyển theo chiều dọc, sử dụng transition.value
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            hideColor
        ),
        start = Offset(0f, transition.value),
        end = Offset(0f, transition.value + 500f) // Điều chỉnh kích thước LED nếu cần
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
                repeatMode = RepeatMode.Restart,
                highlightColor = Color(0xFFAEEA00),
                baseColor = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.congratulations_you_have_completed),
                style = MaterialTheme.typography.displaySmall
            )

            AnimatedGradientText(
                repeatMode = RepeatMode.Reverse,
                highlightColor = Color(0xFFAEEA00),
                baseColor = MaterialTheme.colorScheme.primary,
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
                repeatMode = RepeatMode.Restart,
                highlightColor = Color(0xFFAEEA00),
                baseColor = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.congratulations_you_have_completed),
                style = MaterialTheme.typography.displaySmall
            )

            AnimatedGradientText(
                repeatMode = RepeatMode.Reverse,
                highlightColor = Color(0xFFAEEA00),
                baseColor = MaterialTheme.colorScheme.primary,
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