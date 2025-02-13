package com.example.biofit.view

import android.content.res.Configuration
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class CreateAndUpdateExerciseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                CAUExerciseScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun CAUExerciseScreen() {
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
                title = /*if (/*nếu người dùng nhấn dấu + bên ExerciseActivity*/) {
                    R.string.new_exercise
                } else {*/
                    R.string.exercise,
                //},
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
    var level by rememberSaveable { mutableIntStateOf(value = R.string.amateur) }
    var showLevelDialog by rememberSaveable { mutableStateOf(value = false) }
    var time by rememberSaveable { mutableStateOf(value = "") }
    var caloriesConsumed by rememberSaveable { mutableStateOf(value = "") }

    Column {
        TextField(
            value = exerciseName,
            onValueChange = { exerciseName = it },
            modifier = modifier,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.exercise_name),
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            prefix = {
                Text(
                    text = stringResource(R.string.exercise),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            ),
        )

        TextField(
            value = stringResource(level),
            onValueChange = { level = it.toInt() },
            modifier = modifier,
            readOnly = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End
            ),
            prefix = {
                Text(
                    text = stringResource(R.string.level),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            trailingIcon = {
                IconButton(onClick = { showLevelDialog = true }) {
                    Image(
                        painter = painterResource(R.drawable.btn_back),
                        contentDescription = stringResource(R.string.level),
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

        if (showLevelDialog) {
            Dialog(
                selectedOption = level,
                onOptionSelected = { selectedLevel ->
                    level = selectedLevel
                    showLevelDialog = false
                },
                onDismissRequest = { showLevelDialog = false },
                title = R.string.select_level,
                listOptions = listOf(
                    R.string.amateur,
                    R.string.professional
                ),
                standardPadding = standardPadding
            )
        }

        TextField(
            value = time,
            onValueChange = { time = it },
            modifier = modifier,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End
            ),
            prefix = {
                Text(
                    text = stringResource(R.string.time),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            suffix = {
                Text(
                    text = stringResource(R.string.minutes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            value = caloriesConsumed,
            onValueChange = { caloriesConsumed = it },
            modifier = modifier,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End
            ),
            prefix = {
                Text(
                    text = stringResource(R.string.calories_consumed),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            suffix = {
                Text(
                    text = stringResource(R.string.cal),
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

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun DarkModePreviewInSmallPhone() {
    BioFitTheme {
        CAUExerciseScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun PreviewInLargePhone() {
    BioFitTheme {
        CAUExerciseScreen()
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
private fun PreviewInTablet() {
    BioFitTheme {
        CAUExerciseScreen()
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
private fun LandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CAUExerciseScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun LandscapePreviewInLargePhone() {
    BioFitTheme {
        CAUExerciseScreen()
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
private fun LandscapePreviewInTablet() {
    BioFitTheme {
        CAUExerciseScreen()
    }
}