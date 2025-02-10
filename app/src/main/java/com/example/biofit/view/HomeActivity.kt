package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlin.random.Random

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                HomeScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun HomeScreen() {
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
        Column {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                            start = standardPadding,
                            end = standardPadding,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HomeScreenContent(
                        standardPadding,
                        modifier
                    )
                }
            }

            Row {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BottomBar(
                        onItemSelected = { TODO() },
                        standardPadding = standardPadding
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderBar(modifier: Modifier) {
    val userName = stringResource(R.string.full_name) // Thay tên người dùng từ database vào User name
    val currentDate = stringResource(R.string.current_date) // Thay đổi thành ngày hiện tại
    val currentMonth = stringResource(R.string.current_month) // Thay đổi thành tháng hiện tại
    val currentYear = stringResource(R.string.current_year) // Thay đổi thành năm hiện tại

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.hello) + "$userName!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = stringResource(R.string.today) +
                        "$currentDate / $currentMonth / $currentYear",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(
            onClick = { TODO() },
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.notification),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(
            onClick = { TODO() },
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.calendar),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn {
        item {
            HeaderBar(modifier)
        }

        item {
            Spacer(modifier = Modifier.height(standardPadding))
            OverviewAndSearchBar(
                standardPadding,
                modifier
            )
        }

        item {
            Spacer(modifier = Modifier.height(standardPadding))
            DailyMenu(
                standardPadding,
                modifier
            )
        }

        item {
            Spacer(modifier = Modifier.height(standardPadding))
            DailyGoals(
                standardPadding,
                modifier
            )
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

fun getTargetCalories(): Float {
    return 1000f // Thay đổi thành lượng calo mục tiêu
}

@Composable
fun OverviewAndSearchBar(
    standardPadding: Dp,
    modifier: Modifier
) {
    val loadedCalories = 430 // Thay đổi thành lượng calo đã nạp
    val targetCalories = getTargetCalories()
    val nutrients = listOf(
        Triple(R.string.protein, 400, 1000), // Thay đổi thành protein
        Triple(R.string.powdered_sugar, 1700, 1000), // Thay đổi thành đường
        Triple(R.string.fat, 700, 1000), // Thay đổi thành chất béo
        Triple(R.string.salt, 600, 1000), // Thay đổi thành muối
        Triple(R.string.fiber, 500, 1000) // Thay đổi thành chất xơ
    )
    var search by rememberSaveable { mutableStateOf("") }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier.padding(standardPadding),
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.overview),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                )

                Button(
                    onClick = { TODO() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(standardPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RemainingCaloriesChart(
                        loadedCalories.toFloat(),
                        targetCalories.toFloat(),
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondary,
                        Color(0xFF960000),
                        MaterialTheme.colorScheme.onPrimary,
                        stringResource(R.string.exceeded_calories),
                        stringResource(R.string.remaining_calories),
                        standardPadding
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(standardPadding),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_loaded_cal),
                            contentDescription = "Loaded Calories",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = stringResource(R.string.loaded) + " $loadedCalories " +
                                    stringResource(R.string.cal),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_target_cal),
                            contentDescription = "Target Calories",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = stringResource(R.string.target) + " $targetCalories " +
                                    stringResource(R.string.cal),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            LazyRow(
                modifier = Modifier.padding(
                    top = standardPadding * 3,
                    bottom = standardPadding
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                nutrients.forEach { (nameRes, loaded, target) ->
                    item {
                        NutritionChart(
                            loaded.toFloat(),
                            target.toFloat(),
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.onSecondary,
                            Color(0xFF960000),
                            standardPadding
                        )

                        Spacer(modifier = Modifier.width(standardPadding * 3))

                        Column {
                            Text(
                                text = stringResource(nameRes),
                                color = if (loaded > target) {
                                    Color(0xFF960000)
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                },
                                style = MaterialTheme.typography.labelSmall
                            )

                            Text(
                                text = "$loaded / $target" + stringResource(R.string.gam),
                                color = if (loaded > target) {
                                    Color(0xFF960000)
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(standardPadding))

    OutlinedTextField(
        value = search,
        onValueChange = { search = it },
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodySmall,
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            IconButton(
                onClick = { TODO() }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { TODO() },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_scan),
                    contentDescription = "Scan",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        keyboardActions = KeyboardActions(
            onDone = { /*TODO*/ },
            onGo = { /*TODO*/ },
            onNext = { /*TODO*/ },
            onPrevious = { /*TODO*/ },
            onSearch = { /*TODO*/ },
            onSend = { /*TODO*/ }
        ),
        singleLine = true,
        maxLines = 1,
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun RemainingCaloriesChart(
    loadedCalories: Float,
    targetCalories: Float,
    circleColor: Color,
    progressColor: Color,
    exceededColor: Color,
    remainingCaloriesColor: Color,
    exceededCaloriesText: String,
    remainingCaloriesText: String,
    standardPadding: Dp
) {
    Canvas(
        modifier = Modifier.size(standardPadding * 12)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 - 20f

        val totalAngle = 360f
        val targetAngle = (targetCalories / targetCalories) * totalAngle
        val loadedAngle = (loadedCalories / targetCalories) * totalAngle
        val exceeded = loadedCalories > targetCalories

        drawCircle(
            color = circleColor.copy(alpha = 0.3f),
            center = center,
            radius = radius,
            style = Stroke(width = radius / 1.7f)
        )

        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = if (exceeded) targetAngle else loadedAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = radius / 2f,
                cap = StrokeCap.Round
            )
        )

        if (exceeded) {
            val exceededAngle = (loadedCalories - targetCalories) / targetCalories * totalAngle

            drawArc(
                color = exceededColor,
                startAngle = -90f + targetAngle,
                sweepAngle = exceededAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(
                    width = radius / 2f,
                    cap = StrokeCap.Round
                )
            )
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                if (exceeded) {
                    "${((targetCalories - loadedCalories) * (-1)).toInt()}"
                } else {
                    "${((targetCalories - loadedCalories)).toInt()}"
                },
                center.x,
                center.y + standardPadding.value,
                android.graphics.Paint().apply {
                    textSize = radius / 2
                    color = if (exceeded) {
                        exceededColor.toArgb()
                    } else {
                        remainingCaloriesColor.toArgb()
                    }
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )

            drawText(
                if (exceeded) {
                    exceededCaloriesText
                } else {
                    remainingCaloriesText
                },
                center.x,
                center.y + standardPadding.value * 3.5f,
                android.graphics.Paint().apply {
                    textSize = radius / 7
                    color = if (exceeded) {
                        exceededColor.toArgb()
                    } else {
                        remainingCaloriesColor.toArgb()
                    }
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}

@Composable
fun NutritionChart(
    loadedValue: Float,
    targetValue: Float,
    circleColor: Color,
    progressColor: Color,
    exceededColor: Color,
    standardPadding: Dp
) {
    Canvas(
        modifier = Modifier.size(standardPadding * 3)
    ) {
        val center = Offset(size.width, size.height / 2)
        val radius = size.minDimension / 2f

        val totalAngle = 360f
        val targetAngle = (targetValue / targetValue) * totalAngle
        val loadedAngle = (loadedValue / targetValue) * totalAngle
        val exceeded = loadedValue > targetValue

        drawCircle(
            color = circleColor.copy(alpha = 0.3f),
            center = center,
            radius = radius,
            style = Stroke(
                width = radius / 1.7f,
                cap = StrokeCap.Round
            )
        )

        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = if (exceeded) targetAngle else loadedAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = radius / 2f,
                cap = StrokeCap.Round
            )
        )

        if (exceeded) {
            val exceededAngle = (loadedValue - targetValue) / targetValue * totalAngle

            drawArc(
                color = exceededColor,
                startAngle = -90f + targetAngle,
                sweepAngle = exceededAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(
                    width = radius / 2f,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

@Composable
fun DailyMenu(
    standardPadding: Dp,
    modifier: Modifier
) {
    val loadedBreakfast = 100 // Thay đổi thành lượng calo đã nạp
    val targetBreakfast = 1000 // Thay đổi thành lượng calo mục tiêu
    val foodBreakfast = "Food name, ..." // Thay đổi thành tên món ăn
    val loadedLunch = 100 // Thay đổi thành lượng calo đã nạp
    val targetLunch = 1000 // Thay đổi thành lượng calo mục tiêu
    val foodLunch = "Food name, ..." // Thay đổi thành tên món ăn
    val loadedDinner = 100 // Thay đổi thành lượng calo đã nạp
    val targetDinner = 1000 // Thay đổi thành lượng calo mục tiêu
    val foodDinner = "Food name, ..." // Thay đổi thành tên món ăn
    val loadedSnack = 100 // Thay đổi thành lượng calo đã nạp
    val targetSnack = 1000 // Thay đổi thành lượng calo mục tiêu
    val foodSnack = "Food name, ..." // Thay đổi thành tên món ăn

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        Text(
            text = stringResource(R.string.track_your_daily_menu),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            DailyCard(
                modifier = Modifier.weight(1f),
                headIcon = R.drawable.ic_morning,
                desIcon = R.string.morning,
                title = R.string.morning,
                loaded = loadedBreakfast,
                target = targetBreakfast,
                foodName = foodBreakfast,
                standardPadding = standardPadding
            )

            DailyCard(
                modifier = Modifier.weight(1f),
                headIcon = R.drawable.ic_afternoon,
                desIcon = R.string.afternoon,
                title = R.string.afternoon,
                loaded = loadedLunch,
                target = targetLunch,
                foodName = foodLunch,
                standardPadding = standardPadding
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            DailyCard(
                modifier = Modifier.weight(1f),
                headIcon = R.drawable.ic_evening,
                desIcon = R.string.evening,
                title = R.string.evening,
                loaded = loadedDinner,
                target = targetDinner,
                foodName = foodDinner,
                standardPadding = standardPadding
            )

            DailyCard(
                modifier = Modifier.weight(1f),
                headIcon = R.drawable.ic_snack,
                desIcon = R.string.snack,
                title = R.string.snack,
                loaded = loadedSnack,
                target = targetSnack,
                foodName = foodSnack,
                standardPadding = standardPadding
            )
        }
    }
}

@Composable
fun DailyCard(
    modifier: Modifier,
    headIcon: Int,
    desIcon: Int,
    title: Int,
    loaded: Int,
    target: Int,
    foodName: String,
    standardPadding: Dp
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(headIcon),
                    contentDescription = stringResource(desIcon),
                    modifier = Modifier
                        .padding(top = standardPadding)
                        .size(standardPadding * 3)
                        .weight(1f)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { TODO() }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_add_daily_menu),
                            contentDescription = "Add",
                            modifier = Modifier.size(standardPadding * 1.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(standardPadding))

            Text(
                text = stringResource(title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "$loaded / $target" + stringResource(R.string.gam),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(standardPadding))

            Text(
                text = foodName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(standardPadding))
        }
    }
}

fun getBurnedCalories(): Float {
    return Random.nextInt(100, 300).toFloat() // Thay đổi thành lượng calo tiêu thụ
}

@Composable
fun DailyGoals(
    standardPadding: Dp,
    modifier: Modifier
) {
    val loadedWater = 1.4 // Thay đổi thành lượng nước đã nạp
    val targetWater = 2 // Thay đổi thành lượng nước mục tiêu
    val burnedCalories = getBurnedCalories()
    val targetBurnCalories = 200f

    val latestWeight = 70.5f // Thay đổi thành cân nặng mới nhất
    val currentDate = 7 // Thay đổi thành ngày hiện tại
    val currentMonth = 2 // Thay đổi thành tháng hiện tại
    val currentYear = 2025 // Thay đổi thành năm hiện tại
    val sixDaysBefore = currentDate - 6 // Thay đổi thành ngày 6 ngày trước
    val fiveDaysBefore = currentDate - 5 // Thay đổi thành ngày 5 ngày trước
    val fourDaysBefore = currentDate - 4 // Thay đổi thành ngày 4 ngày trước
    val threeDaysBefore = currentDate - 3 // Thay đổi thành ngày 3 ngày trước
    val twoDaysBefore = currentDate - 2 // Thay đổi thành ngày 2 ngày trước
    val yesterday = currentDate - 1 // Thay đổi thành ngày 1 ngày trước
    val today = currentDate // Thay đổi thành ngày hiện tại

    val weightSixDaysBefore = 69.7f // Thay đổi thành cân nặng của 6 ngày trước
    val weightFiveDaysBefore = 67.5f // Thay đổi thành cân nặng của 5 ngày trước
    val weightFourDaysBefore = 66.5f // Thay đổi thành cân nặng của 4 ngày trước
    val weightThreeDaysBefore = 65.5f // Thay đổi thành cân nặng của 3 ngày trước
    val weightTwoDaysBefore = 66.8f // Thay đổi thành cân nặng của 2 ngày trước
    val weightYesterday = 69.5f // Thay đổi thành cân nặng của ngày hôm qua
    val weightToday = 70.5f // Thay đổi thành cân nặng của hôm nay

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        Text(
            text = stringResource(R.string.daily_goals),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(standardPadding),
                    verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_water),
                            contentDescription = stringResource(R.string.drinking_water),
                            modifier = Modifier.size(standardPadding * 1.5f)
                        )

                        Text(
                            text = stringResource(R.string.drinking_water),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    WaterChart(
                        loadedWater.toFloat(),
                        targetWater.toFloat(),
                        MaterialTheme.colorScheme.secondaryContainer,
                        if (isSystemInDarkTheme()) {
                            Color(0xFF32FCF9)
                        } else {
                            Color(0xFF91FCF9)
                        },
                        if (isSystemInDarkTheme()) {
                            Color(0xFF000096)
                        } else {
                            Color(0xFF0000AF)
                        },
                        standardPadding
                    )

                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                                alpha = 0.3f
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = standardPadding),
                            horizontalArrangement = Arrangement.spacedBy(standardPadding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { TODO() }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_less_water),
                                    contentDescription = "Less water",
                                    modifier = Modifier.size(standardPadding * 1.5f)
                                )
                            }

                            Text(
                                text = "|",
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )

                            IconButton(
                                onClick = { TODO() }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_add_water),
                                    contentDescription = "Add water",
                                    modifier = Modifier.size(standardPadding * 1.5f)
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(standardPadding),
                    verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_exercise),
                            contentDescription = stringResource(R.string.exercise),
                            modifier = Modifier.size(standardPadding * 1.5f)
                        )

                        Text(
                            text = stringResource(R.string.exercise),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    ExerciseChart(
                        burnedCalories.toFloat(),
                        targetBurnCalories.toFloat(),
                        MaterialTheme.colorScheme.secondaryContainer,
                        if (isSystemInDarkTheme()) {
                            Color(0xFF8C3200)
                        } else {
                            Color(0xFFAA4600)
                        },
                        if (isSystemInDarkTheme()) {
                            Color(0xFF960000)
                        } else {
                            Color(0xFFAF0000)
                        },
                        standardPadding
                    )

                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                                alpha = 0.3f
                            )
                        )
                    ) {
                        TextButton(
                            onClick = { TODO() },
                            modifier = Modifier.padding(horizontal = standardPadding * 2)
                        ) {
                            Text(
                                text = stringResource(R.string.activity),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(standardPadding),
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.latest_weight),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row {
                            Text(
                                text = "$latestWeight" + stringResource(R.string.kg) +
                                        " | $currentDate/$currentMonth/$currentYear",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    Button(
                        onClick = { TODO() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.update),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                val weightData = listOf(
                    "$sixDaysBefore/$currentMonth" to weightSixDaysBefore,
                    "$fiveDaysBefore/$currentMonth" to weightFiveDaysBefore,
                    "$fourDaysBefore/$currentMonth" to weightFourDaysBefore,
                    "$threeDaysBefore/$currentMonth" to weightThreeDaysBefore,
                    "$twoDaysBefore/$currentMonth" to weightTwoDaysBefore,
                    "$yesterday/$currentMonth" to weightYesterday,
                    "$today/$currentMonth" to weightToday
                )

                WeightLineChart(weightData)
            }
        }
    }
}

@Composable
fun WaterChart(
    loadedValue: Float,
    targetValue: Float,
    circleColor: Color,
    progressColor: Color,
    exceededColor: Color,
    standardPadding: Dp
) {
    Canvas(
        modifier = Modifier.size(standardPadding * 10)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 - 20f

        val totalAngle = 360f
        val loadedAngle = if (targetValue != 0f) {
            (loadedValue / targetValue) * totalAngle
        } else {
            0f
        }
        val exceeded = loadedValue > targetValue

        drawCircle(
            color = circleColor.copy(alpha = 0.3f),
            center = center,
            radius = radius,
            style = Stroke(width = radius / 1.7f)
        )

        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = if (exceeded) totalAngle else loadedAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = radius / 2f,
                cap = StrokeCap.Round
            )
        )

        if (exceeded) {
            val exceededAngle = if (targetValue != 0f) {
                (loadedValue - targetValue) / targetValue * totalAngle
            } else {
                0f
            }

            drawArc(
                color = exceededColor,
                startAngle = -90f + totalAngle,
                sweepAngle = exceededAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(
                    width = radius / 2f,
                    cap = StrokeCap.Round
                )
            )
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "%.1f".format(loadedValue),
                center.x,
                center.y + standardPadding.value,
                android.graphics.Paint().apply {
                    textSize = radius / 2
                    color = if (exceeded) {
                        exceededColor.toArgb()
                    } else {
                        progressColor.toArgb()
                    }
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )

            drawText(
                "%.1fL".format(targetValue),
                center.x,
                center.y + standardPadding.value * 3.5f,
                android.graphics.Paint().apply {
                    textSize = radius / 7
                    color = if (exceeded) {
                        progressColor.toArgb()
                    } else {
                        circleColor.toArgb()
                    }
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}

@Composable
fun ExerciseChart(
    loadedValue: Float,
    targetValue: Float,
    circleColor: Color,
    progressColor: Color,
    exceededColor: Color,
    standardPadding: Dp
) {
    Canvas(
        modifier = Modifier.size(standardPadding * 10)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 - 20f

        val totalAngle = 300f
        val loadedAngle = if (targetValue > 0) {
            (loadedValue / targetValue) * totalAngle
        } else {
            0f
        }
        val exceeded = loadedValue > targetValue

        drawArc(
            color = circleColor.copy(alpha = 0.3f),
            startAngle = -240f,
            sweepAngle = totalAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = radius / 1.7f,
                cap = StrokeCap.Round
            )
        )

        drawArc(
            color = if (exceeded) exceededColor else progressColor,
            startAngle = -240f,
            sweepAngle = loadedAngle.coerceAtMost(totalAngle),
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = radius / 2f,
                cap = StrokeCap.Round
            )
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "%.1f".format(loadedValue),
                center.x,
                center.y + standardPadding.value,
                android.graphics.Paint().apply {
                    textSize = radius / 2
                    color = if (exceeded) exceededColor.toArgb() else progressColor.toArgb()
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )

            drawText(
                "%.1fcal".format(targetValue),
                center.x,
                center.y + standardPadding.value * 3.5f,
                android.graphics.Paint().apply {
                    textSize = radius / 7
                    color = if (exceeded) progressColor.toArgb() else circleColor.toArgb()
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}

@Composable
fun WeightLineChart(weightData: List<Pair<String, Float>>) {
    val chartEntryModel = remember {
        ChartEntryModelProducer(
            weightData.mapIndexed { index, data ->
                entryOf(index.toFloat(), data.second)
            }
        )
    }

    val lineChart = lineChart(
        lines = listOf(
            LineChart.LineSpec(
                lineColor = 0xFF0000FF.toInt(),
                lineThicknessDp = 3f,
            )
        )
    )

    val marker = rememberMarkerComponent()

    val chartScrollState = rememberChartScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Chart(
            chart = lineChart,
            model = chartEntryModel.getModel(),
            startAxis = rememberStartAxis(
                label = textComponent(
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            ),
            bottomAxis = rememberBottomAxis(
                label = textComponent(
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
                valueFormatter = { value, _ ->
                    weightData.getOrNull(value.toInt())?.first ?: ""
                }
            ),
            marker = marker,
            isZoomEnabled = true,
            chartScrollState = chartScrollState
        )
    }
}

@Composable
fun rememberMarkerComponent(): MarkerComponent {
    val label = textComponent()
    val indicator = shapeComponent(Shapes.pillShape)
    val guideline = lineComponent()

    return remember {
        MarkerComponent(
            label = label,
            indicator = indicator,
            guideline = guideline
        )
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
private fun HomePortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        HomeScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun HomePortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        HomeScreen()
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
private fun HomePortraitScreenPreviewInTablet() {
    BioFitTheme {
        HomeScreen()
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
private fun HomeLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        HomeScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun HomeLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        HomeScreen()
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
private fun HomeLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        HomeScreen()
    }
}