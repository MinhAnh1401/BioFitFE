package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class TargetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                TargetScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun TargetScreen() {
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
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarSetting(
                onBackClick = { TODO() },
                title = R.string.target,
                middleButton = null,
                rightButton = {
                    TextButton(
                        onClick = { TODO() }
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.inverseSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                standardPadding = standardPadding
            )

            TargetContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun TargetContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    var goal by rememberSaveable { mutableIntStateOf(R.string.muscle_gain) }
    var showGoalDialog by rememberSaveable { mutableStateOf(false) }
    var protein by rememberSaveable { mutableStateOf("") }
    var carb by rememberSaveable { mutableStateOf("") }
    var weeklyGoal by rememberSaveable {
        mutableIntStateOf(
            if (goal == R.string.muscle_gain) {
                R.string.gain_025_kg_week
            } else {
                R.string.lose_025_kg_week
            }
        )
    }
    var showWeeklyGoalDialog by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(goal) {
        weeklyGoal = if (goal == R.string.muscle_gain) {
            R.string.gain_025_kg_week
        } else {
            R.string.lose_025_kg_week
        }
    }
    var intensityOfExercise by rememberSaveable { mutableIntStateOf(R.string.hard_working) }
    var showIntensityOfExerciseDialog by rememberSaveable { mutableStateOf(false) }


    val nutritionTargetList = listOf(
        R.string.calorie_and_macro_goals,
        R.string.calories_distribution
    )

    var caloriesDuringExercise by rememberSaveable { mutableStateOf("0") }

    var targetWater by rememberSaveable { mutableStateOf("0") }
    var healthyDiet by rememberSaveable { mutableStateOf("0") }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
    ) {
        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.weight),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                TextField(
                    value = stringResource(goal),
                    onValueChange = { goal = it.toInt() },
                    modifier = modifier,
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.your_goal),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showGoalDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.your_goal),
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                if (showGoalDialog) {
                    Dialog(
                        selectedOption = goal,
                        onOptionSelected = { selectedGoal ->
                            goal = selectedGoal
                            showGoalDialog = false
                        },
                        onDismissRequest = { showGoalDialog = false },
                        title = R.string.select_goal,
                        listOptions = listOf(
                            R.string.weight_loss,
                            R.string.muscle_gain
                        ),
                        standardPadding = standardPadding
                    )
                }

                TextField(
                    value = protein,
                    onValueChange = { protein = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.protein),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.percentage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )

                TextField(
                    value = carb,
                    onValueChange = { carb = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.carbohydrate),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.percentage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )

                TextField(
                    value = stringResource(weeklyGoal),
                    onValueChange = { weeklyGoal = it.toInt() },
                    modifier = modifier,
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.weekly_goal),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showWeeklyGoalDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.weekly_goal),
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                if (showWeeklyGoalDialog) {
                    Dialog(
                        selectedOption = weeklyGoal,
                        onOptionSelected = { selectedWeeklyGoal ->
                            weeklyGoal = selectedWeeklyGoal
                            showWeeklyGoalDialog = false
                        },
                        onDismissRequest = { showWeeklyGoalDialog = false },
                        title = R.string.select_weekly_goal,
                        listOptions = if (goal == R.string.muscle_gain) {
                            listOf(
                                R.string.gain_025_kg_week,
                                R.string.gain_05_kg_week,
                                R.string.gain_075_kg_week,
                                R.string.gain_1_kg_week
                            )
                        } else {
                            listOf(
                                R.string.lose_025_kg_week,
                                R.string.lose_05_kg_week,
                                R.string.lose_075_kg_week,
                                R.string.lose_1_kg_week
                            )
                        },
                        standardPadding = standardPadding
                    )
                }

                TextField(
                    value = stringResource(intensityOfExercise),
                    onValueChange = { intensityOfExercise = it.toInt() },
                    modifier = modifier,
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.intensity_of_exercise),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showIntensityOfExerciseDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.intensity_of_exercise),
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                if (showIntensityOfExerciseDialog) {
                    Dialog(
                        selectedOption = intensityOfExercise,
                        onOptionSelected = { selectedIntensityOfExercise ->
                            intensityOfExercise = selectedIntensityOfExercise
                            showIntensityOfExerciseDialog = false
                        },
                        onDismissRequest = { showIntensityOfExerciseDialog = false },
                        title = R.string.select_intensity,
                        listOptions = listOf(
                            R.string.sedentary,
                            R.string.gentle,
                            R.string.hard_working,
                            R.string.very_good
                        ),
                        standardPadding = standardPadding
                    )
                }
            }
        }

        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.nutrition_target),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                nutritionTargetList.forEach { title ->
                    Row(
                        modifier = Modifier.padding(top = standardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(title),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = standardPadding),
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(
                            onClick = { TODO() }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = "Add button",
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.exercise) + "*",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                TextField(
                    value = caloriesDuringExercise,
                    onValueChange = { caloriesDuringExercise = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.calories_during_exercise),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.kcal),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )
            }
        }

        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.drinking_water) + "*",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                TextField(
                    value = targetWater,
                    onValueChange = { targetWater = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.target),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.ml),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )

                TextField(
                    value = healthyDiet,
                    onValueChange = { healthyDiet = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.healthy_diet),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.ml),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
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

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun TargetScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TargetScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun TargetScreenPreviewInLargePhone() {
    BioFitTheme {
        TargetScreen()
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
private fun TargetScreenPreviewInTablet() {
    BioFitTheme {
        TargetScreen()
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
private fun TargetScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TargetScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TargetScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        TargetScreen()
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
private fun TargetScreenLandscapePreviewInTablet() {
    BioFitTheme {
        TargetScreen()
    }
}