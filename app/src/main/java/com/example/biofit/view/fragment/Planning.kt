package com.example.biofit.view.fragment

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.widget.NumberPicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.biofit.R
import com.example.biofit.view.activity.CalorieTodayActivity
import com.example.biofit.view.activity.CreatePlanningActivity
import com.example.biofit.view.activity.EditExerciseActivity
import com.example.biofit.view.activity.ExerciseViewActivity
import com.example.biofit.view.activity.MealsListActivity
import com.example.biofit.view.sub_components.CalendarSelector
import com.example.biofit.view.sub_components.getStandardPadding
import com.example.biofit.view.ui_theme.BioFitTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun PlanningScreen() {
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
            PlanningHeaderBar(
                rightButton = {
                    IconButton(
                        onClick = { TODO() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.calendar),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                standardPadding = standardPadding
            )

            PlanningScreenContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun PlanningScreenContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

//    val userPlanning = databaseHelper.getUserPlanById(1, 0)
    val userPlanning = 0

    val selectedIntensityOption = rememberSaveable { mutableIntStateOf(R.string.low) }
    var expandedIntensity by rememberSaveable { mutableStateOf(false) }
    val optionsIntensity = listOf(
        R.string.low,
        R.string.medium,
        R.string.high
    )
    val filteredOptionsIntensity =
        optionsIntensity.filter { it != selectedIntensityOption.intValue }


    val selectedDietPlanOption = rememberSaveable { mutableIntStateOf(R.string.weight_loss) }
    var expandedDietPlan by rememberSaveable { mutableStateOf(false) }
    val optionsDietPlan = listOf(
        R.string.weight_loss,
        R.string.muscle_gain,
        R.string.healthy_diet
    )
    val filteredOptionsDietPlan = optionsDietPlan.filter { it != selectedDietPlanOption.intValue }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        if (userPlanning == null) {
            item {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.make_a_plan_for_yourself),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = stringResource(R.string.des_planning),
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )

                    TextButton(
                        onClick = {
                            activity?.let {
                                val intent = Intent(it, CreatePlanningActivity::class.java)
                                it.startActivity(intent)
                            }
                        },
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_plus),
                                contentDescription = "Create new plan",
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = stringResource(R.string.create_plan),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        } else {
            item {
                CalendarSelector(
                    onDaySelected = { },
                    standardPadding = standardPadding,
                    modifier = modifier
                )
            }

            item {
                val caloriesData = listOf(
                    "12AM" to 250f,
                    "6AM" to 500f,
                    "12PM" to 300f,
                    "6PM" to 450f,
                    "12PM" to 200f,
                )

                CaloriesLineChart(
                    weightData = caloriesData,
                    standardPadding = standardPadding,
                    modifier = modifier
                )
            }

            item {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            WellnessTrackerCard(
                                onClick = { TODO() },
                                title = stringResource(R.string.workout_sessions),
                                details = "0/7",
                                standardPadding = standardPadding,
                                modifier = modifier
                            )
                        }

                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            var showDialog by rememberSaveable { mutableStateOf(false) }
                            var selectedMinutes by rememberSaveable { mutableIntStateOf(0) }

                            WellnessTrackerCard(
                                onClick = { showDialog = true },
                                title = stringResource(R.string.duration),
                                details = "$selectedMinutes ${stringResource(R.string.min)}",
                                standardPadding = standardPadding,
                                modifier = modifier
                            )

                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    title = {
                                        Text(
                                            text = "Select Duration",
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    },
                                    text = {
                                        NumberPicker(
                                            value = selectedMinutes,
                                            range = 0..120,
                                            onValueChange = { selectedMinutes = it }
                                        )
                                    },
                                    shape = MaterialTheme.shapes.extraLarge,
                                    confirmButton = {
                                        TextButton(
                                            onClick = { showDialog = false }
                                        ) {
                                            Text(
                                                text = "OK",
                                                color = MaterialTheme.colorScheme.primary,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            WellnessTrackerCard(
                                onClick = { expandedIntensity = true },
                                title = stringResource(R.string.intensity),
                                details = stringResource(selectedIntensityOption.intValue),
                                standardPadding = standardPadding,
                                modifier = modifier
                            )

                            DropdownMenu(
                                expanded = expandedIntensity,
                                onDismissRequest = { expandedIntensity = false }
                            ) {
                                filteredOptionsIntensity.forEach { selection ->
                                    DropdownMenuItem(
                                        text = { Text(stringResource(selection)) },
                                        onClick = {
                                            selectedIntensityOption.intValue = selection
                                            expandedIntensity = false
                                        }
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            WellnessTrackerCard(
                                onClick = { expandedDietPlan = true },
                                title = stringResource(R.string.diet_plan),
                                details = stringResource(selectedDietPlanOption.intValue),
                                standardPadding = standardPadding,
                                modifier = modifier
                            )

                            DropdownMenu(
                                expanded = expandedDietPlan,
                                onDismissRequest = { expandedDietPlan = false }
                            ) {
                                filteredOptionsDietPlan.forEach { selection ->
                                    DropdownMenuItem(
                                        text = { Text(stringResource(selection)) },
                                        onClick = {
                                            selectedDietPlanOption.intValue = selection
                                            expandedDietPlan = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.suggested_meals),
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleSmall
                        )

                        IconButton(
                            onClick = { TODO() }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    val morningMeals = listOf(
                        "Bakery",
                        "Milk"
                    )
                    val afternoonMeals = listOf(
                        "Brown rice",
                        "Boiled eggs"
                    )
                    val eveningMeals = listOf(
                        "Brown rice",
                        "Meat"
                    )
                    val snackMeals = listOf(
                        "Salad",
                        "Fruit"
                    )

                    Column(
                        modifier = modifier
                    ) {
                        Row {
                            Column(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .clickable {
                                        activity?.let {
                                            val intent = Intent(it, MealsListActivity::class.java)
                                            intent.putExtra("SESSION_TOGGLE", R.string.morning)
                                            it.startActivity(intent)
                                        }
                                    }
                            ) {
                                SuggestedMeals(
                                    suggestedMealsSession = R.string.morning,
                                    suggestedMealsImg = R.drawable.img_food_default,
                                    suggestedMeals = morningMeals,
                                    standardPadding = standardPadding
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .clickable {
                                        activity?.let {
                                            val intent = Intent(it, MealsListActivity::class.java)
                                            intent.putExtra("SESSION_TOGGLE", R.string.afternoon)
                                            it.startActivity(intent)
                                        }
                                    }
                            ) {
                                SuggestedMeals(
                                    suggestedMealsSession = R.string.afternoon,
                                    suggestedMealsImg = R.drawable.img_food_default,
                                    suggestedMeals = afternoonMeals,
                                    standardPadding = standardPadding
                                )
                            }
                        }

                        Row {
                            Column(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .clickable {
                                        activity?.let {
                                            val intent = Intent(it, MealsListActivity::class.java)
                                            intent.putExtra("SESSION_TOGGLE", R.string.evening)
                                            it.startActivity(intent)
                                        }
                                    }
                            ) {
                                SuggestedMeals(
                                    suggestedMealsSession = R.string.evening,
                                    suggestedMealsImg = R.drawable.img_food_default,
                                    suggestedMeals = eveningMeals,
                                    standardPadding = standardPadding
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .clickable {
                                        activity?.let {
                                            val intent = Intent(it, MealsListActivity::class.java)
                                            intent.putExtra("SESSION_TOGGLE", R.string.snack)
                                            it.startActivity(intent)
                                        }
                                    }
                            ) {
                                SuggestedMeals(
                                    suggestedMealsSession = R.string.snack,
                                    suggestedMealsImg = R.drawable.img_food_default,
                                    suggestedMeals = snackMeals,
                                    standardPadding = standardPadding
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Text(
                        text = stringResource(R.string.workout_suggestion),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )

                    val workoutSuggestion = listOf(
                        Pair(Pair(0, "Exercise 1"), Triple(15, 120f, 0)),
                        Pair(Pair(1, "Exercise 2"), Triple(30, 150f, 1)),
                        Pair(Pair(2, "Exercise 3"), Triple(45, 200f, 2))
                    )

                    workoutSuggestion.forEach { aiExercise ->
                        WorkoutSuggestion(
                            session = when (aiExercise.first.first) {
                                0 -> R.string.morning
                                1 -> R.string.afternoon
                                else -> R.string.evening
                            },
                            exerciseName = aiExercise.first.second,
                            time = aiExercise.second.first,
                            calories = aiExercise.second.second,
                            intensity = when (aiExercise.second.third) {
                                0 -> R.string.low
                                1 -> R.string.medium
                                else -> R.string.high
                            },
                            onClickCard = {
                                activity?.let {
                                    val intent = Intent(it, EditExerciseActivity::class.java)
                                    intent.putExtra(
                                        "SESSION_TITLE",
                                        when (aiExercise.first.first) {
                                            0 -> R.string.morning
                                            1 -> R.string.afternoon
                                            else -> R.string.evening
                                        }
                                    )
                                    it.startActivity(intent)
                                }
                            },
                            onClickButton = {
                                activity?.let {
                                    val intent = Intent(it, ExerciseViewActivity::class.java)
                                    it.startActivity(intent)
                                }
                            },
                            standardPadding = standardPadding
                        )
                    }
                }
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

@Composable
fun PlanningHeaderBar(
    rightButton: (@Composable () -> Unit)? = null,
    standardPadding: Dp
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(standardPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_planning),
            contentDescription = "Planning",
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(R.string.nutrition_planning),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )

        rightButton?.invoke()
    }
}

@Composable
fun CaloriesLineChart(
    weightData: List<Pair<String, Float>>,
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val chartEntryModel = remember {
        ChartEntryModelProducer(
            weightData.mapIndexed { index, data ->
                entryOf(index.toFloat(), data.second)
            }
        )
    }

    val lineChart = lineChart()

    val marker = rememberMarkerComponent()

    val chartScrollState = rememberChartScrollState()

    Column(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable {
                activity?.let {
                    val intent = Intent(it, CalorieTodayActivity::class.java)
                    it.startActivity(intent)
                }
            },
        verticalArrangement = Arrangement.spacedBy(standardPadding),
    ) {
        Text(
            text = stringResource(R.string.calories),
            modifier = Modifier.padding(standardPadding),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "1200kcal",
            modifier = Modifier.padding(horizontal = standardPadding),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displaySmall
        )

        Row(
            modifier = Modifier.padding(horizontal = standardPadding)
        ) {
            Text(
                text = stringResource(R.string.today) + " ",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "+10%",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Chart(
            chart = lineChart,
            model = chartEntryModel.getModel(),
            modifier = Modifier.padding(standardPadding),
            bottomAxis = rememberBottomAxis(
                label = textComponent(
                    color = MaterialTheme.colorScheme.onBackground,
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
fun WellnessTrackerCard(
    onClick: () -> Unit,
    title: String,
    details: String,
    standardPadding: Dp,
    modifier: Modifier
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                shape = MaterialTheme.shapes.extraLarge
            ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = modifier
                .height(standardPadding * 14)
                .padding(standardPadding * 2),
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = details,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                setOnValueChangedListener { _, _, newVal -> onValueChange(newVal) }
            }
        },
        update = { it.value = value }
    )
}

@Composable
fun SuggestedMeals(
    suggestedMealsSession: Int,
    suggestedMealsImg: Int,
    suggestedMeals: List<String>,
    standardPadding: Dp
) {
    Column(
        modifier = Modifier.padding(standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        Text(
            text = stringResource(suggestedMealsSession),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Image(
            painter = painterResource(suggestedMealsImg),
            contentDescription = "Suggested meals",
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    shape = MaterialTheme.shapes.extraLarge
                ),
            contentScale = ContentScale.Crop
        )

        Text(
            text = suggestedMeals.joinToString(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun WorkoutSuggestion(
    session: Int,
    exerciseName: String,
    time: Int,
    calories: Float,
    intensity: Int,
    onClickCard: () -> Unit,
    onClickButton: () -> Unit,
    standardPadding: Dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        Text(
            text = stringResource(session),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Card(
            onClick = onClickCard,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    shape = MaterialTheme.shapes.large
                ),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(standardPadding),
                horizontalArrangement = Arrangement.spacedBy(standardPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(standardPadding / 2)
                ) {
                    Text(
                        text = exerciseName,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = "${time}min, ${calories}kcal, ${stringResource(R.string.intensity)}" +
                                ": ${stringResource(intensity)}",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = onClickButton,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = stringResource(R.string.start),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelLarge
                    )
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
private fun PlanningScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        PlanningScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PlanningScreenPreviewInLargePhone() {
    BioFitTheme {
        PlanningScreen()
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
private fun PlanningScreenPreviewInTablet() {
    BioFitTheme {
        PlanningScreen()
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
private fun PlanningScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        PlanningScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PlanningScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        PlanningScreen()
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
private fun PlanningScreenLandscapePreviewInTablet() {
    BioFitTheme {
        PlanningScreen()
    }
}