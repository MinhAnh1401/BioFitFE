package com.example.biofit.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.data.model.dto.UserDTO
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.navigation.BarChart
import com.example.biofit.navigation.StackedBarChart
import com.example.biofit.navigation.WeekNavigationBar
import com.example.biofit.navigation.getMonthlyMacroTable
import com.example.biofit.navigation.getPercentages
import com.example.biofit.navigation.weeklyData
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.patrykandpatrick.vico.core.extension.sumOf
import java.math.RoundingMode
import java.time.LocalDate

@Composable
fun OverviewWeekScreen() {
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
            OverviewWeekContent(
                standardPadding,
                modifier
            )
        }
    }
}

@Composable
fun OverviewWeekContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val userData = UserSharedPrefsHelper.getUserData(context) ?: UserDTO.default()
    val targetCalories = when (userData.gender) {
        0 -> when (userData.getAgeInt(userData.dateOfBirth)) {
            in 0..45 -> 2000f
            else -> 1500f
        }

        1 -> when (userData.getAgeInt(userData.dateOfBirth)) {
            in 0..30 -> 1500f
            else -> 1000f
        }

        else -> 0f
    }
    val targetCaloriesWeek =
        targetCalories * 7 // Lấy tổng lượng calo mục tiêu từ thứ 2 đến chủ nhật
    val targetCaloriesDay = targetCalories // Tính lượng calo mục tiêu trung bình của 1 ngày
    val actualCaloriesWeek =
        weeklyData.sumOf { it.calories } // Lấy tổng lượng calo thực tế từ thứ 2 đến chủ nhật
    val actualCaloriesDay = (actualCaloriesWeek / 7)
        .toBigDecimal()
        .setScale(2, RoundingMode.HALF_UP) // Tính lượng calo thực tế trung bình của 1 ngày
    val monthlyCaloriesStatistics = listOf(
        stringResource(R.string.target_calories_for_the_week) to targetCaloriesWeek,
        stringResource(R.string.target_calories_day) to targetCaloriesDay,
        stringResource(R.string.actual_calories_for_the_week) to actualCaloriesWeek,
        stringResource(R.string.actual_calories_day) to actualCaloriesDay
    )

    val monthlyMacroTable = getMonthlyMacroTable()
    val percentagesMacro = getPercentages(monthlyMacroTable.map { it.value })
    val percentagesTargetMacro = getPercentages(monthlyMacroTable.map { it.targetValue })

    Column(
        modifier = modifier.padding(top = standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        WeekNavigationBar(
            selectedDate = selectedDate,
            onDateChange = { newDate -> selectedDate = newDate },
            standardPadding
        )

        Column {
            Text(
                text = stringResource(R.string.average_calorie_count),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )

            BarChart(
                weeklyData,
                MaterialTheme.colorScheme.onBackground,
                standardPadding
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            monthlyCaloriesStatistics.forEach { (title, calories) ->
                Row {
                    Text(
                        text = title,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "$calories ${stringResource(R.string.kcal)}",
                        color = if (title == stringResource(R.string.target_calories)) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (title == stringResource(R.string.target_calories_day)) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }
            }
        }

        Column {
            Text(
                text = stringResource(R.string.average_macro_count),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall
            )

            StackedBarChart(
                weeklyData,
                MaterialTheme.colorScheme.onBackground,
                standardPadding
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            monthlyMacroTable.forEachIndexed { index, (icon, title, value, targetValue) ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Row(
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
                                text = "$title ($value g / $targetValue g)",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            text = "${
                                percentagesMacro[index]
                                    .toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                            }%",
                            modifier = Modifier.weight(0.25f),
                            color = MaterialTheme.colorScheme.onBackground,
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

                    if (index != monthlyMacroTable.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
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
private fun OverviewWeekScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        OverviewWeekScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun OverviewWeekScreenPreviewInLargePhone() {
    BioFitTheme {
        OverviewWeekScreen()
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
private fun OverviewWeekScreenPreviewInTablet() {
    BioFitTheme {
        OverviewWeekScreen()
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
private fun OverviewWeekScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        OverviewWeekScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun OverviewWeekScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        OverviewWeekScreen()
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
private fun OverviewWeekScreenLandscapePreviewInTablet() {
    BioFitTheme {
        OverviewWeekScreen()
    }
}