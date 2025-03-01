package com.example.biofit.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.navigation.DailyCountChart
import com.example.biofit.navigation.getDailyCalories
import com.example.biofit.navigation.getDailyMacroTable
import com.example.biofit.navigation.getPercentages
import com.example.biofit.ui.components.CalendarSelector
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import java.math.RoundingMode

@Composable
fun OverviewDayScreen() {
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OverviewDayContent(
                standardPadding,
                modifier
            )
        }
    }
}

@Composable
fun OverviewDayContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val dailyCaloriesTable = getDailyCalories()
    val dailyCalories = dailyCaloriesTable.map { it.calories }

    val foodCaloriesIntake = dailyCalories[0] + dailyCalories[1] +
            dailyCalories[2] + dailyCalories[3]
    val netCalories = foodCaloriesIntake - getBurnedCalories()
    val dailyCaloriesStatistics = listOf(
        stringResource(R.string.food_calories_intake) to foodCaloriesIntake,
        stringResource(R.string.exercise_calories) to getBurnedCalories(),
        stringResource(R.string.net_calories) to netCalories,
        stringResource(R.string.target_calories) to getTargetCalories()
    )

    val dailyMacroTable = getDailyMacroTable()
    val dailyMacroValues = dailyMacroTable.map { it.value }
    val percentagesMacro = getPercentages(dailyMacroTable.map { it.value })
    val percentagesTargetMacro = getPercentages(dailyMacroTable.map { it.targetValue })

    Column(
        modifier = modifier.padding(top = standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        CalendarSelector(
            onDaySelected = { },
            standardPadding = standardPadding,
            modifier = modifier
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    shape = MaterialTheme.shapes.extraLarge
                ),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(standardPadding),
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.daily_calorie_count),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Column(
                        modifier = Modifier.weight(0.5f),
                        verticalArrangement = Arrangement.spacedBy(standardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(standardPadding * 2))

                        if (dailyCaloriesTable.map { it.calories } != listOf(0f, 0f, 0f, 0f)) {
                            DailyCountChart(
                                dailyCalories,
                                colors = listOf(
                                    Color(0xFFF0CA45),
                                    Color(0xFF75F94D),
                                    Color(0xFF60D6FA),
                                    Color(0xFFBE7DF7)
                                ),
                                MaterialTheme.colorScheme.scrim,
                                standardPadding
                            )
                        }

                        Spacer(modifier = Modifier.height(standardPadding * 2))
                    }

                    Column(
                        modifier = Modifier.weight(0.5f),
                        verticalArrangement = Arrangement.spacedBy(standardPadding),
                    ) {
                        dailyCaloriesTable.forEach { (icon, session, calories) ->
                            Column(
                                modifier = Modifier.padding(start = standardPadding)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(standardPadding),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = icon,
                                        contentDescription = null
                                    )

                                    Text(
                                        text = session,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Text(
                                    text = "$calories ${stringResource(R.string.cal)}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = standardPadding * 4),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            dailyCaloriesStatistics.forEach { (title, calories) ->
                Row {
                    Text(
                        text = title,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "$calories ${stringResource(R.string.cal)}",
                        color = if (title == stringResource(R.string.target_calories)) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (title == stringResource(R.string.exercise_calories)) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    shape = MaterialTheme.shapes.extraLarge
                ),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.daily_marco_count),
                    modifier = Modifier.padding(
                        top = standardPadding,
                        start = standardPadding,
                        end = standardPadding
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = standardPadding,
                            vertical = standardPadding * 4
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (dailyMacroTable.map { it.value } != listOf(0f, 0f, 0f)) {
                        DailyCountChart(
                            dailyMacroValues,
                            colors = listOf(
                                Color(0xFFEE7975),
                                Color(0xFF60D6FA),
                                Color(0xFFF8CA45)
                            ),
                            MaterialTheme.colorScheme.scrim,
                            standardPadding
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(0.5f))

                    Text(
                        text = stringResource(R.string.average_consumption),
                        modifier = Modifier.weight(0.25f),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )

                    Text(
                        text = stringResource(R.string.target),
                        modifier = Modifier.weight(0.25f),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f))

                dailyMacroTable.forEachIndexed { index, (icon, title, value, targetValue) ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(standardPadding),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = standardPadding),
                            horizontalArrangement = Arrangement.spacedBy(standardPadding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(0.5f),
                                horizontalArrangement = Arrangement.spacedBy(standardPadding),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = icon,
                                    contentDescription = title
                                )

                                Text(
                                    text = "$title (${value}g / ${targetValue}g)",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            Text(
                                text = "${
                                    percentagesMacro[index]
                                        .toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                                }%",
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Card(
                                modifier = Modifier.weight(0.25f),
                                shape = MaterialTheme.shapes.extraLarge,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = "${
                                        percentagesTargetMacro[index]
                                            .toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                                    }%",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(standardPadding / 2),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        if (index != dailyMacroTable.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(standardPadding / 2))
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun OverviewDayScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        OverviewDayScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun OverviewDayScreenPreviewInLargePhone() {
    BioFitTheme {
        OverviewDayScreen()
    }
}

@Preview(
    device = "spec:parent=Nexus 10,orientation=portrait",
    locale = "vi",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun OverviewDayScreenPreviewInTablet() {
    BioFitTheme {
        OverviewDayScreen()
    }
}

@Preview(
    device = "spec:parent=pixel,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun OverviewDayScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        OverviewDayScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun OverviewDayScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        OverviewDayScreen()
    }
}

@Preview(
    device = "spec:parent=Nexus 10",
    locale = "vi",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun OverviewDayScreenLandscapePreviewInTablet() {
    BioFitTheme {
        OverviewDayScreen()
    }
}