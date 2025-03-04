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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.biofit.ui.components.ItemCard
import com.example.biofit.ui.components.SelectionDialog
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

class CreateAndUpdateExerciseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val getExtra = intent.getStringExtra("EXERCISE")
        setContent {
            BioFitTheme {
                if (getExtra != null) {
                    CAUExerciseScreen(getExtra)
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun CAUExerciseScreen(getExtra: String) {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    val title by rememberSaveable { mutableStateOf(getExtra) }

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
                title = if (title != "") {
                    title
                } else {
                    stringResource(R.string.new_exercise)
                },
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

            CAUExerciseContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun CAUExerciseContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    var exerciseName by rememberSaveable { mutableStateOf(value = "") }
    var level by rememberSaveable { mutableStateOf("") }
    var showLevelDialog by rememberSaveable { mutableStateOf(value = false) }
    var time by rememberSaveable { mutableStateOf(value = "") }
    var caloriesConsumed by rememberSaveable { mutableStateOf(value = "") }

    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        OutlinedTextField(
            value = exerciseName,
            onValueChange = { exerciseName = it },
            modifier = modifier,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.End
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.exercise_name),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            prefix = {
                Text(
                    text = stringResource(R.string.exercise),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )

        ItemCard(
            onClick = { showLevelDialog = true },
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
                    text = if (level == "") {
                        stringResource(R.string.select_level)
                    } else {
                        level
                    },
                    modifier = Modifier.weight(1f),
                    color = if (level == "") {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    style = MaterialTheme.typography.bodySmall
                )

                IconButton(onClick = { showLevelDialog = true }) {
                    Image(
                        painter = painterResource(R.drawable.btn_back),
                        contentDescription = stringResource(R.string.level),
                        modifier = Modifier.rotate(270f)
                    )
                }
            }
        }

        if (showLevelDialog) {
            SelectionDialog(
                selectedOption = level,
                onOptionSelected = { selectedLevel ->
                    level = selectedLevel
                    showLevelDialog = false
                },
                onDismissRequest = { showLevelDialog = false },
                title = R.string.select_level,
                listOptions = listOf(
                    stringResource(R.string.amateur),
                    stringResource(R.string.professional)
                ),
                standardPadding = standardPadding
            )
        }

        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            modifier = modifier,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.End
            ),
            prefix = {
                Text(
                    text = stringResource(R.string.time),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            suffix = {
                Text(
                    text = stringResource(R.string.min),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )

        OutlinedTextField(
            value = caloriesConsumed,
            onValueChange = { caloriesConsumed = it },
            modifier = modifier,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.End
            ),
            prefix = {
                Text(
                    text = stringResource(R.string.calories_consumed),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            suffix = {
                Text(
                    text = stringResource(R.string.cal),
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
}

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun CAUExerciseScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CAUExerciseScreen("")
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun CAUExerciseScreenPreviewInLargePhone() {
    BioFitTheme {
        CAUExerciseScreen("")
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
private fun CAUExerciseScreenPreviewInTablet() {
    BioFitTheme {
        CAUExerciseScreen("")
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
private fun CAUExerciseScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CAUExerciseScreen("")
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CAUExerciseScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        CAUExerciseScreen("")
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
private fun CAUExerciseScreenLandscapePreviewInTablet() {
    BioFitTheme {
        CAUExerciseScreen("")
    }
}