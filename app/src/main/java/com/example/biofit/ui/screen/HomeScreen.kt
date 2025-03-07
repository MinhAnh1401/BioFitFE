package com.example.biofit.ui.screen

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.model.dto.UserDTO
import com.example.biofit.data.utils.DailyLogSharedPrefsHelper
import com.example.biofit.navigation.OverviewActivity
import com.example.biofit.ui.activity.CaloriesTargetActivity
import com.example.biofit.ui.activity.ExerciseActivity
import com.example.biofit.ui.activity.NotificationActivity
import com.example.biofit.ui.activity.OverviewExerciseActivity
import com.example.biofit.ui.activity.TrackActivity
import com.example.biofit.ui.activity.UpdateWeightActivity
import com.example.biofit.ui.components.MainCard
import com.example.biofit.ui.components.SubCard
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.DailyLogViewModel
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.random.Random

@Composable
fun HomeScreen(userData: UserDTO) {
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
            HomeContent(
                userData,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun HeaderBar(
    userData: UserDTO,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val userName = if (userData.fullName.isNullOrEmpty()) {
        ""
    } else {
        ", ${userData.fullName}"
    }

    val currentDate = LocalDate.now()
    val formatter = if (Locale.current.language == "vi") {
        DateTimeFormatter.ofPattern("dd MMMM yyyy")
    } else {
        DateTimeFormatter.ofPattern("MMMM dd yyyy")
    }

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
                text = "${stringResource(R.string.today)}, ${currentDate.format(formatter)}",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(
            onClick = {
                activity?.let {
                    val intent = Intent(it, NotificationActivity::class.java)
                    it.startActivity(intent)
                }
            },
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.notification),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        var selectedDate by rememberSaveable { mutableStateOf("") }
        var showDatePicker by rememberSaveable { mutableStateOf(false) }

        IconButton(
            onClick = { showDatePicker = true },
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.calendar),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            LaunchedEffect(Unit) {
                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        showDatePicker = false
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }
}

@Composable
fun HomeContent(
    userData: UserDTO,
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            HeaderBar(
                userData,
                modifier = modifier
            )
        }

        item {
            OverviewAndSearchBar(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }

        item {
            DailyMenu(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }

        item {
            DailyGoals(
                userData,
                standardPadding,
                modifier
            )
        }

        item {
            Spacer(
                modifier = Modifier.padding(
                    bottom = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateBottomPadding() * 2
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
    val context = LocalContext.current
    val activity = context as? Activity

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

    MainCard(
        onClick = {
            activity?.let {
                val intent = Intent(it, OverviewActivity::class.java)
                it.startActivity(intent)
            }
        },
        modifier = modifier
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
                    onClick = {
                        activity?.let {
                            val intent = Intent(it, CaloriesTargetActivity::class.java)
                            it.startActivity(intent)
                        }
                    },
                    modifier = Modifier.widthIn(
                        min = standardPadding * 10
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
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
                        loadedCalories = loadedCalories.toFloat(),
                        targetCalories = targetCalories,
                        circleColor = MaterialTheme.colorScheme.primaryContainer,
                        progressColor = MaterialTheme.colorScheme.inversePrimary,
                        exceededColor = MaterialTheme.colorScheme.error,
                        remainingCaloriesColor = MaterialTheme.colorScheme.onPrimary,
                        exceededCaloriesText = stringResource(R.string.exceeded_calories),
                        remainingCaloriesText = stringResource(R.string.remaining_calories),
                        standardPadding = standardPadding
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
                        Spacer(modifier = Modifier.width(standardPadding))

                        CircularProgressIndicator(
                            progress = { loaded.toFloat() / target.toFloat() },
                            modifier = Modifier.size(standardPadding * 4),
                            color = MaterialTheme.colorScheme.inversePrimary,
                            strokeWidth = standardPadding / 1.5f,
                            trackColor = MaterialTheme.colorScheme.primaryContainer
                        )

                        Spacer(modifier = Modifier.width(standardPadding))

                        Column {
                            Text(
                                text = stringResource(nameRes),
                                color = if (loaded > target) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                },
                                style = MaterialTheme.typography.labelSmall
                            )

                            Text(
                                text = "$loaded / $target" + stringResource(R.string.gam),
                                color = if (loaded > target) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Spacer(modifier = Modifier.width(standardPadding))
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(standardPadding))

    OutlinedTextField(
        value = search,
        onValueChange = { search = it },
        modifier = modifier.shadow(
            elevation = 6.dp,
            shape = MaterialTheme.shapes.large
        ),
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { /*TODO*/ }
        ),
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedBorderColor = Color.Transparent
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
        modifier = Modifier.size(standardPadding * 10)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 - 20f

        val totalAngle = 360f
        val targetAngle = (targetCalories / targetCalories) * totalAngle
        val loadedAngle = (loadedCalories / targetCalories) * totalAngle
        val exceeded = loadedCalories > targetCalories

        drawCircle(
            color = circleColor,
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
                Paint().apply {
                    textSize = radius / 2
                    color = if (exceeded) {
                        exceededColor.toArgb()
                    } else {
                        remainingCaloriesColor.toArgb()
                    }
                    textAlign = Paint.Align.CENTER
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
                Paint().apply {
                    textSize = radius / 7
                    color = if (exceeded) {
                        exceededColor.toArgb()
                    } else {
                        remainingCaloriesColor.toArgb()
                    }
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}

@Composable
fun DailyMenu(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

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
                onClick = {
                    activity?.let {
                        val intent = Intent(it, TrackActivity::class.java)
                        intent.putExtra("SESSION_TITLE", R.string.morning)
                        it.startActivity(intent)
                    }
                },
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
                onClick = {
                    activity?.let {
                        val intent = Intent(it, TrackActivity::class.java)
                        intent.putExtra("SESSION_TITLE", R.string.afternoon)
                        it.startActivity(intent)
                    }
                },
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
                onClick = {
                    activity?.let {
                        val intent = Intent(it, TrackActivity::class.java)
                        intent.putExtra("SESSION_TITLE", R.string.evening)
                        it.startActivity(intent)
                    }
                },
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
                onClick = {
                    activity?.let {
                        val intent = Intent(it, TrackActivity::class.java)
                        intent.putExtra("SESSION_TITLE", R.string.snack)
                        it.startActivity(intent)
                    }
                },
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
    onClick: () -> Unit,
    modifier: Modifier,
    headIcon: Int,
    desIcon: Int,
    title: Int,
    loaded: Int,
    target: Int,
    foodName: String,
    standardPadding: Dp
) {
    SubCard(
        onClick = onClick,
        modifier = modifier
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(standardPadding),
                    horizontalAlignment = Alignment.End
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_add_daily_menu),
                        contentDescription = "Add",
                        modifier = Modifier.size(standardPadding * 1.5f)
                    )
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
    userData: UserDTO,
    standardPadding: Dp,
    modifier: Modifier,
    dailyLogViewModel: DailyLogViewModel = viewModel(),
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val loadedWater = 1.4 // Thay đổi thành lượng nước đã nạp
    val targetWater = 2 // Thay đổi thành lượng nước mục tiêu
    val burnedCalories = getBurnedCalories()
    val targetBurnCalories = 200f

    dailyLogViewModel.updateUserId(userData.userId)
    var latestWeight = DailyLogSharedPrefsHelper.getDailyLog(context)?.weight
    var latestWeightState by remember { mutableStateOf(DailyLogSharedPrefsHelper.getDailyLog(context)?.weight) }
    val today = LocalDate.now()
    val weightDataState by dailyLogViewModel.weightDataState
    LaunchedEffect(userData.userId) {
        dailyLogViewModel.getWeightHistory(userData.userId)
    }
    LaunchedEffect(weightDataState) {
        latestWeightState = DailyLogSharedPrefsHelper.getDailyLog(context)?.weight
    }
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == "DAILY_LOG") {
                val updatedWeight = DailyLogSharedPrefsHelper.getDailyLog(context)?.weight
                latestWeightState = updatedWeight
            }
        }
    }

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
            MainCard(
                onClick = {},
                modifier = Modifier.weight(1f)
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
                        sizeChart = standardPadding * 10,
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
                        "L",
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

            MainCard(
                onClick = {
                    activity?.let {
                        val intent = Intent(it, OverviewExerciseActivity::class.java)
                        it.startActivity(intent)
                    }
                },
                modifier = Modifier.weight(1f)
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
                            contentDescription = stringResource(R.string.activity),
                            modifier = Modifier.size(standardPadding * 1.5f)
                        )

                        Text(
                            text = stringResource(R.string.activity),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    ExerciseChart(
                        burnedCalories,
                        targetBurnCalories,
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
                            onClick = {
                                activity?.let {
                                    val intent = Intent(it, ExerciseActivity::class.java)
                                    it.startActivity(intent)
                                }
                            },
                            modifier = Modifier.padding(horizontal = standardPadding * 2)
                        ) {
                            Text(
                                text = stringResource(R.string.exercise),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        SubCard(modifier = modifier) {
            Column(
                modifier = Modifier.padding(standardPadding),
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(standardPadding / 2)
                    ) {
                        Text(
                            text = stringResource(R.string.latest_weight),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(standardPadding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$latestWeight" + stringResource(R.string.kg) +
                                        " | $today",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelSmall
                            )

                            var isRotating by remember { mutableStateOf(false) }
                            val rotation by animateFloatAsState(
                                targetValue = if (isRotating) 360f else 0f,
                                animationSpec = tween(durationMillis = 500, easing = LinearEasing),
                                finishedListener = { isRotating = false } // Reset trạng thái sau khi xoay xong
                            )

                            IconButton(
                                onClick = {
                                    Log.d("HomeScreen", "today: $today")
                                    Log.d("HomeScreen", "latestWeightDate: ${DailyLogSharedPrefsHelper.getDailyLog(context)?.date}")
                                    isRotating = true
                                    dailyLogViewModel.getWeightHistory(userData.userId)
                                    latestWeight = DailyLogSharedPrefsHelper.getDailyLog(context)?.weight
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.graphicsLayer(rotationZ = rotation)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            activity?.let {
                                val intent = Intent(it, UpdateWeightActivity::class.java)
                                it.startActivity(intent)
                            }
                        },
                        modifier = Modifier.widthIn(
                            min = standardPadding * 10
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.update),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                WeightLineChart(weightDataState)
            }
        }
    }
}

@Composable
fun WaterChart(
    sizeChart: Dp,
    loadedValue: Float,
    targetValue: Float,
    circleColor: Color,
    progressColor: Color,
    exceededColor: Color,
    unit: String,
    standardPadding: Dp
) {
    Canvas(
        modifier = Modifier.size(sizeChart)
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
                Paint().apply {
                    textSize = radius / 2
                    color = if (exceeded) {
                        exceededColor.toArgb()
                    } else {
                        progressColor.toArgb()
                    }
                    textAlign = Paint.Align.CENTER
                }
            )

            drawText(
                "%.1f$unit".format(targetValue),
                center.x,
                center.y + standardPadding.value * 3.5f,
                Paint().apply {
                    textSize = radius / 7
                    color = if (exceeded) {
                        progressColor.toArgb()
                    } else {
                        circleColor.toArgb()
                    }
                    textAlign = Paint.Align.CENTER
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
                Paint().apply {
                    textSize = radius / 2
                    color = if (exceeded) exceededColor.toArgb() else progressColor.toArgb()
                    textAlign = Paint.Align.CENTER
                }
            )

            drawText(
                "%.1fcal".format(targetValue),
                center.x,
                center.y + standardPadding.value * 3.5f,
                Paint().apply {
                    textSize = radius / 7
                    color = if (exceeded) progressColor.toArgb() else circleColor.toArgb()
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}

@Composable
fun WeightLineChart(weightData: List<Pair<String, Float>>) {
    val chartEntryModel = remember(weightData) {
        ChartEntryModelProducer(
            weightData.mapIndexed { index, data ->
                entryOf(index.toFloat(), data.second)
            }
        )
    }

    val lineChart = lineChart(
        lines = listOf(
            LineChart.LineSpec(
                lineColor = 0xFF34A853.toInt(),
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
                    color = MaterialTheme.colorScheme.onSurface,
                )
            ),
            bottomAxis = rememberBottomAxis(
                label = textComponent(
                    color = MaterialTheme.colorScheme.onSurface,
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
        HomeScreen(
            userData = UserDTO(
                userId = 0,
                fullName = "John",
                email = "john@email.com",
                gender = R.string.male,
                height = 180f,
                weight = 70f,
                targetWeight = 75f,
                dateOfBirth = "2004-01-01",
                avatar = "",
                createdAccount = "2025-01-01"
            )
        )
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
        HomeScreen(
            userData = UserDTO(
                userId = 0,
                fullName = "John",
                email = "john@email.com",
                gender = R.string.male,
                height = 180f,
                weight = 70f,
                targetWeight = 75f,
                dateOfBirth = "2004-01-01",
                avatar = "",
                createdAccount = "2025-01-01"
            )
        )
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
        HomeScreen(
            userData = UserDTO(
                userId = 0,
                fullName = "John",
                email = "john@email.com",
                gender = R.string.male,
                height = 180f,
                weight = 70f,
                targetWeight = 75f,
                dateOfBirth = "2004-01-01",
                avatar = "",
                createdAccount = "2025-01-01"
            )
        )
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
        HomeScreen(
            userData = UserDTO(
                userId = 0,
                fullName = "John",
                email = "john@email.com",
                gender = R.string.male,
                height = 180f,
                weight = 70f,
                targetWeight = 75f,
                dateOfBirth = "2004-01-01",
                avatar = "",
                createdAccount = "2025-01-01"
            )
        )
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
        HomeScreen(
            userData = UserDTO(
                userId = 0,
                fullName = "John",
                email = "john@email.com",
                gender = R.string.male,
                height = 180f,
                weight = 70f,
                targetWeight = 75f,
                dateOfBirth = "2004-01-01",
                avatar = "",
                createdAccount = "2025-01-01"
            )
        )
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
        HomeScreen(
            userData = UserDTO(
                userId = 0,
                fullName = "John",
                email = "john@email.com",
                gender = R.string.male,
                height = 180f,
                weight = 70f,
                targetWeight = 75f,
                dateOfBirth = "2004-01-01",
                avatar = "",
                createdAccount = "2025-01-01"
            )
        )
    }
}