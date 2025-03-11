package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.DefaultDialog
import com.example.biofit.ui.components.ItemCard
import com.example.biofit.ui.components.SelectionDialog
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
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
    val context = LocalContext.current
    val activity = context as? Activity

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
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(R.string.target),
                middleButton = null,
                rightButton = {
                    TextButton(
                        onClick = { activity?.finish() }
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.primary,
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
    val context = LocalContext.current
    val activity = context as? Activity

    var goal by rememberSaveable { mutableStateOf("") }
    var showGoalDialog by rememberSaveable { mutableStateOf(false) }
    var protein by rememberSaveable { mutableStateOf("") }
    var carb by rememberSaveable { mutableStateOf("") }
    var weeklyGoal by rememberSaveable { mutableStateOf("") }
    var showWeeklyGoalDialog by rememberSaveable { mutableStateOf(false) }
    var showErrorWeeklyDialog by rememberSaveable { mutableStateOf(false) }
    var intensityOfExercise by rememberSaveable { mutableStateOf("") }
    var showIntensityOfExerciseDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(goal) { weeklyGoal = "" }


    val nutritionTargetList = listOf(
        R.string.calorie_and_macro_goals,
        R.string.calories_distribution
    )

    var caloriesDuringExercise by rememberSaveable { mutableStateOf("0") }

    var targetWater by rememberSaveable { mutableStateOf("0") }
    var healthyDiet by rememberSaveable { mutableStateOf("0") }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
    ) {
        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.weight),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                ItemCard(
                    onClick = { showGoalDialog = true },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = standardPadding,
                                vertical = standardPadding / 4
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (goal == "") {
                                stringResource(R.string.select_goal)
                            } else {
                                goal
                            },
                            modifier = Modifier.weight(1f),
                            color = if (goal == "") {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showGoalDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.goals),
                                modifier = Modifier.rotate(270f)
                            )
                        }
                    }
                }

                if (showGoalDialog) {
                    SelectionDialog(
                        selectedOption = goal,
                        onOptionSelected = { selectedGoal ->
                            goal = selectedGoal
                            showGoalDialog = false
                        },
                        onDismissRequest = { showGoalDialog = false },
                        title = R.string.select_goal,
                        listOptions = listOf(
                            stringResource(R.string.weight_loss),
                            stringResource(R.string.muscle_gain)
                        ),
                        standardPadding = standardPadding
                    )
                }

                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.protein),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.percentage),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )

                OutlinedTextField(
                    value = carb,
                    onValueChange = { carb = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.carbohydrate),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.percentage),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )

                ItemCard(
                    onClick = {
                        if (goal == "") {
                            showErrorWeeklyDialog = true
                        } else {
                            showWeeklyGoalDialog = true
                        }
                    },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = standardPadding,
                                vertical = standardPadding / 4
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (weeklyGoal == "") {
                                stringResource(R.string.select_weekly_goal)
                            } else {
                                weeklyGoal
                            },
                            modifier = Modifier.weight(1f),
                            color = if (weeklyGoal == "") {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showWeeklyGoalDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.weekly_goal),
                                modifier = Modifier.rotate(270f)
                            )
                        }
                    }
                }

                if (showWeeklyGoalDialog) {
                    SelectionDialog(
                        selectedOption = weeklyGoal,
                        onOptionSelected = { selectedWeeklyGoal ->
                            weeklyGoal = selectedWeeklyGoal
                            showWeeklyGoalDialog = false
                        },
                        onDismissRequest = { showWeeklyGoalDialog = false },
                        title = R.string.select_weekly_goal,
                        listOptions = if (goal == stringResource(R.string.muscle_gain)) {
                            listOf(
                                stringResource(R.string.gain_025_kg_week),
                                stringResource(R.string.gain_05_kg_week),
                                stringResource(R.string.gain_075_kg_week),
                                stringResource(R.string.gain_1_kg_week)
                            )
                        } else {
                            listOf(
                                stringResource(R.string.lose_025_kg_week),
                                stringResource(R.string.lose_05_kg_week),
                                stringResource(R.string.lose_075_kg_week),
                                stringResource(R.string.lose_1_kg_week)
                            )
                        },
                        standardPadding = standardPadding
                    )
                }

                if (showErrorWeeklyDialog) {
                    DefaultDialog(
                        title = R.string.please_select_a_goal_before_weekly_goal,
                        actionTextButton = R.string.ok,
                        actionTextButtonColor = MaterialTheme.colorScheme.primary,
                        onClickActionButton = { showErrorWeeklyDialog = false },
                        onDismissRequest = { showErrorWeeklyDialog = false },
                        standardPadding = standardPadding
                    )
                }

                ItemCard(
                    onClick = { showIntensityOfExerciseDialog = true },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = standardPadding,
                                vertical = standardPadding / 4
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (intensityOfExercise == "") {
                                stringResource(R.string.select_intensity)
                            } else {
                                intensityOfExercise
                            },
                            modifier = Modifier.weight(1f),
                            color = if (intensityOfExercise == "") {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showIntensityOfExerciseDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.intensity_of_exercise),
                                modifier = Modifier.rotate(270f)
                            )
                        }
                    }
                }

                if (showIntensityOfExerciseDialog) {
                    SelectionDialog(
                        selectedOption = intensityOfExercise,
                        onOptionSelected = { selectedIntensityOfExercise ->
                            intensityOfExercise = selectedIntensityOfExercise
                            showIntensityOfExerciseDialog = false
                        },
                        onDismissRequest = { showIntensityOfExerciseDialog = false },
                        title = R.string.select_intensity,
                        listOptions = listOf(
                            stringResource(R.string.sedentary),
                            stringResource(R.string.gentle),
                            stringResource(R.string.hard_working),
                            stringResource(R.string.very_good)
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
                            onClick = {
                                activity?.let {
                                    val intent = Intent(
                                        it,
                                        if (title == R.string.calorie_and_macro_goals) {
                                            CaloriesTargetActivity::class.java
                                        } else {
                                            CaloriesDistributionActivity::class.java
                                        }
                                    )
                                    it.startActivity(intent)
                                }
                            }
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
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.exercise) + "*",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedTextField(
                    value = caloriesDuringExercise,
                    onValueChange = { caloriesDuringExercise = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.calories_during_exercise),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.kcal),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    ),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
            }
        }

        /*item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.drinking_water) + "*",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedTextField(
                    value = targetWater,
                    onValueChange = { targetWater = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.target),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.ml),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )

                OutlinedTextField(
                    value = healthyDiet,
                    onValueChange = { healthyDiet = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.healthy_diet),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.ml),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(onGo = { TODO() }),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
            }
        }*/

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