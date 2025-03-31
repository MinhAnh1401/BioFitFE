package com.example.biofit.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseItem(
    exercise: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    standardPadding: Dp,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            ),
        verticalArrangement = Arrangement.spacedBy(standardPadding / 2),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )

            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Add button",
                modifier = Modifier
                    .padding(standardPadding)
                    .size(standardPadding)
                    .rotate(180f),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
    }
}

@Composable
fun OverviewExerciseCard(
    exerciseName: String,
    level: Int,
    intensity: Int,
    time: Int,
    calories: Float,
    session: Int,
    standardPadding: Dp
) {
    val levelString = when (level) {
        0 -> stringResource(R.string.amateur)
        1 -> stringResource(R.string.professional)
        else -> stringResource(R.string.unknown)
    }
    val intensityString = when (intensity) {
        0 -> stringResource(R.string.low)
        1 -> stringResource(R.string.medium)
        2 -> stringResource(R.string.high)
        else -> stringResource(R.string.unknown)
    }

    val showMore = remember { mutableStateOf(false) }

    ItemCard(
        onClick = { showMore.value = !showMore.value },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(standardPadding),
            horizontalArrangement = Arrangement.spacedBy(standardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    when (session) {
                        0 -> R.drawable.cloud_sun_fill
                        1 -> R.drawable.sun_max_fill
                        else -> R.drawable.cloud_moon_fill
                    }
                ),
                contentDescription = stringResource(R.string.session),
                modifier = Modifier.size(standardPadding * 2f),
                tint = when (session) {
                    0 -> Color(0xFFFFAB00)
                    1 -> Color(0xFFDD2C00)
                    else -> Color(0xFF2962FF)
                }
            )

            Text(
                text = exerciseName,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(standardPadding / 2),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding / 4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.timer),
                        contentDescription = stringResource(R.string.time),
                        modifier = Modifier.size(standardPadding),
                        tint = Color(0xFF00C853)
                    )

                    Text(
                        text = "$time ${stringResource(R.string.min)}",
                        color = Color(0xFF00C853),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding / 4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_loaded_cal),
                        contentDescription = stringResource(R.string.calories),
                        modifier = Modifier.size(standardPadding),
                        tint = Color(0xFFFF6D00)
                    )

                    Text(
                        text = "$calories ${stringResource(R.string.kcal)}",
                        color = Color(0xFFFF6D00),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                AnimatedVisibility(
                    visible = showMore.value,
                    enter = slideInVertically { it } + fadeIn() + expandVertically(),
                    exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(standardPadding / 2),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(standardPadding / 4),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.decrease_quotelevel),
                                contentDescription = stringResource(R.string.level),
                                modifier = Modifier.size(standardPadding),
                                tint = Color(0xFFFFAB00)
                            )

                            Text(
                                text = levelString,
                                color = Color(0xFFFFAB00),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(standardPadding / 4),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.figure_highintensity_intervaltraining),
                                contentDescription = stringResource(R.string.intensity),
                                modifier = Modifier.size(standardPadding),
                                tint = Color(0xFFDD2C00)
                            )

                            Text(
                                text = intensityString,
                                color = Color(0xFFDD2C00),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Add button",
                modifier = Modifier
                    .size(standardPadding)
                    .rotate(if (showMore.value) 90f else 270f),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
    showBackground = true
)
@Composable
private fun ExerciseItemDarkModePreview() {
    BioFitTheme {
        Column(
            modifier = Modifier.padding(getStandardPadding().first),
            verticalArrangement = Arrangement.spacedBy(getStandardPadding().first)
        ) {
            ExerciseItem(
                exercise = "Exercise 1",
                onClick = {},
                onLongClick = {},
                standardPadding = getStandardPadding().first,
                modifier = getStandardPadding().second
            )

            OverviewExerciseCard(
                exerciseName = "Exercise 1",
                level = 0,
                intensity = 0,
                time = 30,
                calories = 100f,
                session = 1,
                standardPadding = getStandardPadding().first
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseItemPreview() {
    BioFitTheme {
        Column(
            modifier = Modifier.padding(getStandardPadding().first),
            verticalArrangement = Arrangement.spacedBy(getStandardPadding().first)
        ) {
            ExerciseItem(
                exercise = "Exercise 1",
                onClick = {},
                onLongClick = {},
                standardPadding = getStandardPadding().first,
                modifier = getStandardPadding().second
            )

            OverviewExerciseCard(
                exerciseName = "Exercise 1",
                level = 0,
                intensity = 0,
                time = 30,
                calories = 100f,
                session = 1,
                standardPadding = getStandardPadding().first
            )
        }
    }
}