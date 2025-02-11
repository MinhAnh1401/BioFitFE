package com.example.biofit.view

import android.content.res.Configuration
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import com.patrykandpatrick.vico.core.extension.sumOf
import java.math.RoundingMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class OverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                OverviewScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun OverviewScreen() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f
    val modifier = if (screenWidth > screenHeight) {
        Modifier.width(((screenWidth + screenHeight) / 2).dp)
    } else {
        Modifier.fillMaxWidth()
    }

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
            TopBarSetting(
                onBackClick = { TODO() }, // Xử lý sự kiện khi người dùng nhấn nút Back
                title =  R.string.calorie_statistics,
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            Spacer(modifier = Modifier.height(standardPadding))

            OverviewContent(
                standardPadding,
                modifier
            )
        }
    }
}

@Composable
fun DayMonthToggleButton(
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    standardPadding: Dp
) {
    val options = listOf(
        R.string.day,
        R.string.month
    )

    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(standardPadding / 4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = standardPadding / 2),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = option),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun OverviewContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    var selectedOption by rememberSaveable { mutableIntStateOf(R.string.day) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = modifier
            ) {
                DayMonthToggleButton(
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it },
                    standardPadding = standardPadding
                )
            }
        }

        item {
            when (selectedOption) {
                R.string.day -> OverviewDayContent(
                    standardPadding,
                    modifier
                )

                R.string.month -> OverviewMonthContent(
                    standardPadding,
                    modifier
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.padding(
                    bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() * 2
                            + standardPadding
                )
            )
        }
    }
}

data class DailyCalories(
    val icon: Painter,
    val session: String,
    val calories: Float
)

// Dữ liệu về lượng calo hàng ngày
val dailyCaloriesData = listOf(
    Triple(
        R.drawable.ic_morning_chart,
        R.string.morning,
        Random.nextInt(150, 300).toFloat()/*Lượng calo đã nạp trong buổi sáng*/
    ),
    Triple(
        R.drawable.ic_afternoon_chart,
        R.string.afternoon,
        Random.nextInt(175, 350).toFloat()
    ),
    Triple(
        R.drawable.ic_evening_chart,
        R.string.evening,
        Random.nextInt(125, 250).toFloat()
    ),
    Triple(
        R.drawable.ic_snack_chart,
        R.string.snack,
        Random.nextInt(50, 200).toFloat()
    )
)

// Chuyển đổi dữ liệu thành danh sách DailyCalories
@Composable
fun getDailyCalories(): List<DailyCalories> {
    return dailyCaloriesData.map { (icon, session, calories) ->
        DailyCalories(
            painterResource(icon),
            stringResource(session),
            calories
        )
    }
}

// Hàm tính phần trăm
fun getPercentages(values: List<Float>): List<Float> {
    val total = values.sum()
    return if (total > 0) values.map { it / total * 100 } else List(values.size) { 0f }
}

data class DailyMacro(
    val icon: Painter,
    val title: String,
    val value: Float,
    val targetValue: Float
)

// Dữ liệu về macro dinh dưỡng
val dailyMacroData = listOf(
    Triple(
        R.drawable.ic_protein,
        R.string.protein,
        Pair(
            Random.nextInt(150, 300).toFloat()/*Lượng nạp vào (g)*/,
            300f/*Lượng mục tiêu (g)*/
        )
    ),
    Triple(
        R.drawable.ic_carbohydrate,
        R.string.carbohydrate,
        Pair(
            Random.nextInt(300, 600).toFloat(),
            600f
        )
    ),
    Triple(
        R.drawable.ic_fat,
        R.string.fat,
        Pair(
            Random.nextInt(50, 100).toFloat(),
            100f
        )
    )
)

// Chuyển đổi dữ liệu thành danh sách DailyMacro
@Composable
fun getDailyMacroTable(): List<DailyMacro> {
    return dailyMacroData.map { (icon, title, values) ->
        DailyMacro(
            painterResource(icon),
            stringResource(title),
            values.first,
            values.second
        )
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
        WeekdaySelector(
            standardPadding,
            modifier
        ) { selectedDate ->
            Log.d("Selected Date", selectedDate.toString()) // Xử lý sự kiện khi ngày được chọn
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
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
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(standardPadding),
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.daily_marco_count),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = standardPadding * 3),
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

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )

                dailyMacroTable.forEachIndexed { index, (icon, title, value, targetValue) ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(standardPadding),
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
                                shape = MaterialTheme.shapes.large,
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
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeekdaySelector(
    standardPadding: Dp,
    modifier: Modifier,
    onDaySelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val weekDays = (0..6).map { today.minusDays(it.toLong()) }.reversed()

    var selectedDate by rememberSaveable { mutableStateOf(today) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDays.forEach { date ->
                val isSelected = date == selectedDate
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHighest
                            }
                        )
                        .clickable {
                            selectedDate = date
                            onDaySelected(date)
                        }
                        .padding(standardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(
                            java.time.format.TextStyle.SHORT,
                            Locale.getDefault()
                        ),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        style = MaterialTheme.typography.labelSmall
                    )

                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
fun DailyCountChart(
    dailyValues: List<Float>,
    colors: List<Color>,
    percentageColor: Color,
    standardPadding: Dp
) {
    val totalCalories = dailyValues.sum().takeIf { it > 0 } ?: 1f
    val percentages = dailyValues.map { it / totalCalories * 100 }
    val angles = percentages.map { it / 100 * 360 }

    Canvas(modifier = Modifier.size(standardPadding * 8)) {
        val radius = size.minDimension / 2f
        val holeRadius = radius * 0.6f
        val gapAngle = 2f
        var startAngle = -90f

        drawCircle(
            color = Color.White,
            center = center,
            radius = radius,
            style = Stroke(width = radius * 1.05f)
        )

        angles.forEachIndexed { index, sweepAngle ->
            if (sweepAngle > 0) {
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle - gapAngle,
                    useCenter = false,
                    style = Stroke(width = radius)
                )

                val midAngle = startAngle + (sweepAngle / 2)
                val textRadius = (radius + holeRadius) / 2
                val textPosition = Offset(
                    x = center.x + textRadius * cos(Math.toRadians(midAngle.toDouble())).toFloat(),
                    y = center.y + textRadius * sin(Math.toRadians(midAngle.toDouble())).toFloat()
                )

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${percentages[index].toBigDecimal().setScale(1, RoundingMode.HALF_UP)}%",
                        textPosition.x,
                        textPosition.y,
                        Paint().apply {
                            color = percentageColor.toArgb()
                            textSize = radius / 5
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }

                startAngle += sweepAngle
            }
        }

        drawCircle(
            color = Color.Transparent,
            radius = holeRadius,
        )
    }
}

data class WeeklyNutrition(
    val date: LocalDate,
    val calories: Float,
    val protein: Float,
    val carbohydrate: Float,
    val fat: Float
)

// 📅 Lấy danh sách ngày thực tế của tuần (T2 → CN)
fun getCurrentWeekDates(): List<LocalDate> {
    val today = LocalDate.now()
    val monday = today.with(DayOfWeek.MONDAY)
    return (0..6).map { monday.plusDays(it.toLong()) }
}

// 🔥 Sinh dữ liệu ngẫu nhiên
val weeklyData = getCurrentWeekDates().map { date ->
    WeeklyNutrition(
        date,
        Random.nextInt(2000, 2500).toFloat(),
        Random.nextInt(100, 300).toFloat(),
        Random.nextInt(300, 600).toFloat(),
        Random.nextInt(50, 100).toFloat()
    ) // Dữ liệu random từ 0 - 3000
}

data class WeeklyMacro(
    val icon: Painter,
    val title: String,
    val value: Float,
    val targetValue: Float
)

val monthlyMacroData = listOf(
    Triple(
        R.drawable.ic_protein, R.string.protein, Pair(
            weeklyData.sumOf { it.protein },
            300f * 7
        )
    ),
    Triple(
        R.drawable.ic_carbohydrate, R.string.carbohydrate, Pair(
            weeklyData.sumOf { it.carbohydrate },
            600f * 7
        )
    ),
    Triple(
        R.drawable.ic_fat, R.string.fat, Pair(
            weeklyData.sumOf { it.fat },
            100f * 7
        )
    )
)

@Composable
fun getMonthlyMacroTable(): List<WeeklyMacro> {
    return monthlyMacroData.map { (icon, title, values) ->
        WeeklyMacro(
            painterResource(icon),
            stringResource(title),
            values.first,
            values.second
        )
    }
}

@Composable
fun OverviewMonthContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val targetCaloriesWeek = 2500 * 7 // Lấy tổng lượng calo mục tiêu từ thứ 2 đến chủ nhật
    val targetCaloriesDay = 2500 // Tính lượng calo mục tiêu trung bình của 1 ngày
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
                style = MaterialTheme.typography.titleSmall
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
                        text = "$calories ${stringResource(R.string.cal)}",
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
                style = MaterialTheme.typography.titleSmall
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
                                style = MaterialTheme.typography.labelSmall
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
                            shape = MaterialTheme.shapes.large,
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

@Composable
fun WeekNavigationBar(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    standardPadding: Dp
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM")

    val startOfWeek = selectedDate.with(WeekFields.of(Locale.getDefault()).firstDayOfWeek)
    val endOfWeek = startOfWeek.plusDays(6)

    val currentWeek = "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = standardPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onDateChange(selectedDate.minusWeeks(1)) },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous Week",
                modifier = Modifier
                    .size(standardPadding * 2)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = currentWeek,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

        IconButton(
            onClick = { onDateChange(selectedDate.plusWeeks(1)) }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next Week",
                modifier = Modifier
                    .size(standardPadding * 2)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun BarChart(
    caloriesData: List<WeeklyNutrition>,
    colorValue: Color,
    standardPadding: Dp
) {
    val maxCalories = (caloriesData.maxOf { it.calories }).let {
        ceil(it / 500) * 500
    }
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(standardPadding * 16)
            .padding(standardPadding)
    ) {
        val barWidth = size.width / (caloriesData.size * 2)
        val maxHeight = size.height - 20.dp.toPx()
        val textPaint = Paint().apply {
            color = colorValue.toArgb()
            textSize = 25f
        }

        val yAxisPadding = 50f

        drawLine(
            color = Color.Gray,
            start = Offset(yAxisPadding, 0f),
            end = Offset(yAxisPadding, size.height),
            strokeWidth = 5f
        )

        val step = 500f
        for (i in 0..(maxCalories.toInt() / step.toInt())) {
            val yValue = i * step
            val yOffset = size.height - (yValue / maxCalories) * maxHeight

            drawLine(
                color = Color.Gray,
                start = Offset(yAxisPadding, yOffset),
                end = Offset(size.width, yOffset),
                strokeWidth = 2f
            )

            drawContext.canvas.nativeCanvas.drawText(
                yValue.toInt().toString(),
                yAxisPadding - 70f,
                yOffset + 10f,
                textPaint
            )
        }

        caloriesData.forEachIndexed { index, data ->
            val barHeight = (data.calories / maxCalories) * maxHeight
            val xOffset = index * barWidth * 2 + barWidth / 2 + yAxisPadding + standardPadding.value

            drawRect(
                color = Color(0xFF397D2E),
                topLeft = Offset(xOffset, size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

            drawContext.canvas.nativeCanvas.drawText(
                data.date.format(dateFormatter),
                xOffset,
                size.height + 40f,
                textPaint
            )
        }
    }
}

@Composable
fun StackedBarChart(
    nutritionData: List<WeeklyNutrition>,
    colorValue: Color,
    standardPadding: Dp
) {
    val maxMacro = (nutritionData.maxOf { it.protein + it.carbohydrate + it.fat }).let {
        ceil(it / 200) * 200
    }
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(standardPadding * 16)
            .padding(standardPadding)
    ) {
        val barWidth = size.width / (nutritionData.size * 2)
        val maxHeight = size.height - 20.dp.toPx()
        val textPaint = Paint().apply {
            color = colorValue.toArgb()
            textSize = 25f
        }
        val yAxisPadding = 50f

        drawLine(
            color = Color.Gray,
            start = Offset(yAxisPadding, 0f),
            end = Offset(yAxisPadding, size.height),
            strokeWidth = 5f
        )

        val step = 200f
        for (i in 0..(maxMacro.toInt() / step.toInt())) {
            val yValue = i * step
            val yOffset = size.height - (yValue / maxMacro) * maxHeight

            drawLine(
                color = Color.Gray,
                start = Offset(yAxisPadding, yOffset),
                end = Offset(size.width, yOffset),
                strokeWidth = 2f
            )

            drawContext.canvas.nativeCanvas.drawText(
                yValue.toInt().toString(),
                yAxisPadding - 70f,
                yOffset + 10f,
                textPaint
            )
        }

        nutritionData.forEachIndexed { index, data ->
            val xOffset = index * barWidth * 2 + barWidth / 2 + yAxisPadding + standardPadding.value
            var currentHeight = size.height

            val nutrientColors = listOf(
                Color(0xFFEE7975),
                Color(0xFF60D6FA),
                Color(0xFFF8CA45)
            )

            val values = listOf(data.protein, data.carbohydrate, data.fat)

            values.forEachIndexed { i, value ->
                val barHeight = (value / maxMacro) * maxHeight
                currentHeight -= barHeight

                drawRect(
                    color = nutrientColors[i],
                    topLeft = Offset(xOffset, currentHeight),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                )
            }

            drawContext.canvas.nativeCanvas.drawText(
                data.date.format(dateFormatter),
                xOffset,
                size.height + 40f,
                textPaint
            )
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
private fun OverviewPortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        OverviewScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun OverviewPortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        OverviewScreen()
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
private fun OverviewPortraitScreenPreviewInTablet() {
    BioFitTheme {
        OverviewScreen()
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
private fun OverviewLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        OverviewScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun OverviewLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        OverviewScreen()
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
private fun OverviewLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        OverviewScreen()
    }
}