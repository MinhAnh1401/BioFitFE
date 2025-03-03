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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.ItemCard
import com.example.biofit.ui.components.SelectionDialog
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.screen.PlanningHeaderBar
import com.example.biofit.ui.theme.BioFitTheme

class CreatePlanningActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                CreatePlanningScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun CreatePlanningScreen() {
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
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
            PlanningHeaderBar(
                rightButton = null,
                standardPadding = standardPadding
            )

            CreatePlanningScreenContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun CreatePlanningScreenContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var goal by rememberSaveable { mutableStateOf(value = "") }
    var showGoalDialog by rememberSaveable { mutableStateOf(value = false) }
    var numberOfDays by rememberSaveable { mutableStateOf(value = "") }
    var diet by rememberSaveable { mutableStateOf(value = "") }
    LaunchedEffect(goal) { diet = "" }
    var showDietDialog by rememberSaveable { mutableStateOf(value = false) }
    /*var exercise by rememberSaveable { mutableStateOf(value = "") }*/
    /*var showExerciseDialog by rememberSaveable { mutableStateOf(value = false) }*/
    var intensity by rememberSaveable { mutableStateOf(value = "") }
    var showIntensityDialog by rememberSaveable { mutableStateOf(value = false) }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Column(
                modifier = modifier.padding(top = standardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.my_plan),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.goals),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                ItemCard(
                    onClick = { showGoalDialog = true },
                    modifier = Modifier.fillMaxWidth()
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
                                contentDescription = stringResource(R.string.intensity),
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
                            stringResource(R.string.muscle_gain),
                            stringResource(R.string.healthy_diet)
                        ),
                        standardPadding = standardPadding
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
                    text = stringResource(R.string.plan_duration),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedTextField(
                    value = numberOfDays,
                    onValueChange = { numberOfDays = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodySmall,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_number_of_days),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = if (numberOfDays == "1") {
                                stringResource(R.string.day).lowercase()
                            } else {
                                stringResource(R.string.days).lowercase()
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = MaterialTheme.shapes.large
                )
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.diet),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                ItemCard(
                    onClick = { showDietDialog = true },
                    modifier = Modifier.fillMaxWidth()
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
                            text = if (diet == "") {
                                stringResource(R.string.select_diet)
                            } else {
                                diet
                            },
                            modifier = Modifier.weight(1f),
                            color = if (diet == "") {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showDietDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.intensity),
                                modifier = Modifier.rotate(270f)
                            )
                        }
                    }
                }

                if (showDietDialog) {
                    SelectionDialog(
                        selectedOption = diet,
                        onOptionSelected = { selectedDiet ->
                            diet = selectedDiet
                            showDietDialog = false
                        },
                        onDismissRequest = { showDietDialog = false },
                        title = R.string.select_diet,
                        listOptions = when (goal) {
                            stringResource(R.string.muscle_gain) ->
                                listOf(
                                    stringResource(R.string.high_protein),
                                    stringResource(R.string.bulking)
                                )

                            stringResource(R.string.weight_loss) ->
                                listOf(
                                    stringResource(R.string.keto),
                                    stringResource(R.string.low_carb)
                                )

                            else ->
                                listOf(
                                    stringResource(R.string.mediterranean),
                                    stringResource(R.string.plant_based)
                                )
                        },
                        standardPadding = standardPadding
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
                    text = stringResource(R.string.workout),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                /*Card(
                    onClick = { showExerciseDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline
                    )
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
                            text = if (exercise == "") {
                                stringResource(R.string.select_exercise)
                            } else {
                                exercise
                            },
                            modifier = Modifier.weight(1f),
                            color = if (exercise == "") {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showExerciseDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.intensity),
                                modifier = Modifier.rotate(270f)
                            )
                        }
                    }
                }

                if (showExerciseDialog) {
                    SelectionDialog(
                        selectedOption = exercise,
                        onOptionSelected = { selectedExercise ->
                            exercise = selectedExercise
                            showExerciseDialog = false
                        },
                        onDismissRequest = { showExerciseDialog = false },
                        title = R.string.select_exercise,
                        listOptions = exerciseList,
                        standardPadding = standardPadding
                    )
                }*/

                ItemCard(
                    onClick = { showIntensityDialog = true },
                    modifier = Modifier.fillMaxWidth()
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
                            text = if (intensity == "") {
                                stringResource(R.string.select_intensity)
                            } else {
                                intensity
                            },
                            modifier = Modifier.weight(1f),
                            color = if (intensity == "") {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showIntensityDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.intensity),
                                modifier = Modifier.rotate(270f)
                            )
                        }
                    }
                }

                if (showIntensityDialog) {
                    SelectionDialog(
                        selectedOption = intensity,
                        onOptionSelected = { selectedIntensity ->
                            intensity = selectedIntensity
                            showIntensityDialog = false
                        },
                        onDismissRequest = { showIntensityDialog = false },
                        title = R.string.select_intensity,
                        listOptions = listOf(
                            stringResource(R.string.low),
                            stringResource(R.string.medium),
                            stringResource(R.string.high)
                        ),
                        standardPadding = standardPadding
                    )
                }
            }
        }

        item {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val weightLoss = stringResource(R.string.weight_loss)
                val muscleGain = stringResource(R.string.muscle_gain)
                val healthyDiet = stringResource(R.string.healthy_diet)

                val mediterranean = stringResource(R.string.mediterranean)
                val plantBased = stringResource(R.string.plant_based)

                val low = stringResource(R.string.low)
                val medium = stringResource(R.string.medium)
                val high = stringResource(R.string.high)

                ElevatedButton(
                    onClick = {
                        activity?.let {
                            val intent = Intent(it, MainActivity::class.java)
                            it.startActivity(intent)
                        }
                    },
                    modifier = Modifier.widthIn(min = standardPadding * 10),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.start_plan),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun CreatePlanningScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CreatePlanningScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CreatePlanningScreenPreviewInLargePhone() {
    BioFitTheme {
        CreatePlanningScreen()
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
private fun CreatePlanningScreenPreviewInTablet() {
    BioFitTheme {
        CreatePlanningScreen()
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
private fun CreatePlanningScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CreatePlanningScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CreatePlanningScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        CreatePlanningScreen()
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
private fun CreatePlanningScreenLandscapePreviewInTablet() {
    BioFitTheme {
        CreatePlanningScreen()
    }
}