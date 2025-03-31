package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
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
                            color = MaterialTheme.colorScheme.primary
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
    val interactionSources = remember { List(3) { MutableInteractionSource() } }
    interactionSources.forEach { source ->
        val isPressed by source.collectIsPressedAsState()
        if (isPressed) {
            showGoalDialog = false
            showWeeklyGoalDialog = false
            showIntensityOfExerciseDialog = false
        }
    }

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
                    style = MaterialTheme.typography.titleLarge
                )

                ItemCard(
                    onClick = {
                        showGoalDialog = !showGoalDialog
                        showWeeklyGoalDialog = false
                        showIntensityOfExerciseDialog = false
                        focusManager.clearFocus()
                    },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = standardPadding / 4
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                showGoalDialog = !showGoalDialog
                                showWeeklyGoalDialog = false
                                showIntensityOfExerciseDialog = false
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_target),
                                contentDescription = stringResource(R.string.level),
                                modifier = Modifier.size(standardPadding * 1.5f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(standardPadding / 2))

                        Text(
                            text = stringResource(R.string.goals),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )

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
                            textAlign = TextAlign.End
                        )

                        IconButton(
                            onClick = {
                                showGoalDialog = !showGoalDialog
                                showWeeklyGoalDialog = false
                                showIntensityOfExerciseDialog = false
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.goals),
                                modifier = Modifier
                                    .size(standardPadding)
                                    .rotate(if (showGoalDialog) 90f else 270f),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = showGoalDialog,
                        enter = slideInVertically { it } + fadeIn() + expandVertically(),
                        exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
                    ) {
                        val listOptions = listOf(
                            stringResource(R.string.weight_loss),
                            stringResource(R.string.muscle_gain)
                        )

                        Column {
                            listOptions.forEach { selectedGoal ->
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.1f
                                    )
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            goal = selectedGoal
                                            showGoalDialog = false
                                        },
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = selectedGoal,
                                        modifier = Modifier.padding(standardPadding),
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    modifier = modifier,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.ic_protein),
                            contentDescription = stringResource(R.string.protein),
                            modifier = Modifier.size(standardPadding * 1.5f)
                        )
                    },
                    prefix = { Text(text = stringResource(R.string.protein)) },
                    suffix = { Text(text = stringResource(R.string.percentage)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    interactionSource = interactionSources[0],
                    shape = MaterialTheme.shapes.large
                )

                OutlinedTextField(
                    value = carb,
                    onValueChange = { carb = it },
                    modifier = modifier,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.ic_carbohydrate),
                            contentDescription = stringResource(R.string.carbohydrate),
                            modifier = Modifier.size(standardPadding * 1.5f)
                        )
                    },
                    prefix = { Text(text = stringResource(R.string.carbohydrate)) },
                    suffix = { Text(text = stringResource(R.string.percentage)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    interactionSource = interactionSources[1],
                    shape = MaterialTheme.shapes.large
                )

                ItemCard(
                    onClick = {
                        if (goal == "") {
                            showErrorWeeklyDialog = true
                        } else {
                            showWeeklyGoalDialog = !showWeeklyGoalDialog
                            showGoalDialog = false
                            showIntensityOfExerciseDialog = false
                            focusManager.clearFocus()
                        }
                    },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = standardPadding / 4
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                showWeeklyGoalDialog = !showWeeklyGoalDialog
                                showGoalDialog = false
                                showIntensityOfExerciseDialog = false
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_target),
                                contentDescription = stringResource(R.string.level),
                                modifier = Modifier.size(standardPadding * 1.5f),
                                tint = Color(0xFF0091EA)
                            )
                        }

                        Spacer(modifier = Modifier.width(standardPadding / 2))

                        Text(
                            text = stringResource(R.string.weekly_goal),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )

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
                            textAlign = TextAlign.End
                        )

                        IconButton(
                            onClick = {
                                showWeeklyGoalDialog = !showWeeklyGoalDialog
                                showGoalDialog = false
                                showIntensityOfExerciseDialog = false
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.weekly_goal),
                                modifier = Modifier
                                    .size(standardPadding)
                                    .rotate(if (showWeeklyGoalDialog) 90f else 270f),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = showWeeklyGoalDialog,
                        enter = slideInVertically { it } + fadeIn() + expandVertically(),
                        exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
                    ) {
                        val listOptions = if (goal == stringResource(R.string.muscle_gain)) {
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
                        }

                        Column {
                            listOptions.forEach { selectedWeeklyGoal ->
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.1f
                                    )
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            weeklyGoal = selectedWeeklyGoal
                                            showWeeklyGoalDialog = false
                                        },
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = selectedWeeklyGoal,
                                        modifier = Modifier.padding(standardPadding),
                                    )
                                }
                            }
                        }
                    }
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
                    onClick = {
                        showIntensityOfExerciseDialog = !showIntensityOfExerciseDialog
                        showGoalDialog = false
                        showWeeklyGoalDialog = false
                        focusManager.clearFocus()
                    },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = standardPadding / 4
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                showIntensityOfExerciseDialog = !showIntensityOfExerciseDialog
                                showGoalDialog = false
                                showWeeklyGoalDialog = false
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.figure_highintensity_intervaltraining),
                                contentDescription = stringResource(R.string.level),
                                modifier = Modifier.size(standardPadding * 1.5f),
                                tint = Color(0xFF2962FF)
                            )
                        }

                        Spacer(modifier = Modifier.width(standardPadding / 2))

                        Text(
                            text = stringResource(R.string.intensity),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )

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
                            textAlign = TextAlign.End
                        )

                        IconButton(
                            onClick = {
                                showIntensityOfExerciseDialog = !showIntensityOfExerciseDialog
                                showGoalDialog = false
                                showWeeklyGoalDialog = false
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.intensity_of_exercise),
                                modifier = Modifier
                                    .size(standardPadding)
                                    .rotate(if (showIntensityOfExerciseDialog) 90f else 270f),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = showIntensityOfExerciseDialog,
                        enter = slideInVertically { it } + fadeIn() + expandVertically(),
                        exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
                    ) {
                        val listOptions = listOf(
                            stringResource(R.string.sedentary),
                            stringResource(R.string.gentle),
                            stringResource(R.string.hard_working),
                            stringResource(R.string.very_good)
                        )

                        Column {
                            listOptions.forEach { selectedIntensityOfExercise ->
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.1f
                                    )
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            intensityOfExercise = selectedIntensityOfExercise
                                            showIntensityOfExerciseDialog = false
                                        },
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = selectedIntensityOfExercise,
                                        modifier = Modifier.padding(standardPadding),
                                    )
                                }
                            }
                        }
                    }
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
                    style = MaterialTheme.typography.titleLarge
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
                            color = MaterialTheme.colorScheme.outline
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
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = "Add button",
                                modifier = Modifier
                                    .size(standardPadding)
                                    .rotate(180f),
                                tint = MaterialTheme.colorScheme.onSurface
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
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = caloriesDuringExercise,
                    onValueChange = { caloriesDuringExercise = it },
                    modifier = modifier,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                    prefix = { Text(text = stringResource(R.string.calories_during_exercise)) },
                    suffix = { Text(text = stringResource(R.string.kcal)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    ),
                    singleLine = true,
                    interactionSource = interactionSources[2],
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