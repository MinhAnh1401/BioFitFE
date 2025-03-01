package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import kotlinx.coroutines.delay
import java.util.Locale

class ExerciseViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                ExerciseViewScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

data class Exercise(
    val name: String,
    val time: Pair<Int, Int>,
    val calories: Float,
    val intensity: Intensity,
    val guide: String,
    val effect: String,
    val state: Boolean
)

enum class Intensity(val resId: Int) {
    LOW(R.string.low),
    MEDIUM(R.string.medium),
    HIGH(R.string.high)
}

@Composable
fun ExerciseViewScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    val exercise = Exercise(
        name = "Exercise 1",
        time = Pair(15, 0),
        calories = 150f,
        intensity = Intensity.LOW,
        guide = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
        effect = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
        state = false
    )

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
                onHomeClick = {
                    activity?.let {
                        val intent = Intent(it, MainActivity::class.java)
                        it.startActivity(intent)
                    }
                },
                title = exercise.name,
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            ExerciseViewContent(
                exercise = exercise,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ExerciseViewContent(
    exercise: Exercise,
    standardPadding: Dp,
    modifier: Modifier
) {
    var exerciseState by rememberSaveable { mutableStateOf(exercise.state) }

    var startButtonState by rememberSaveable { mutableStateOf(false) }
    var pauseButtonState by rememberSaveable { mutableStateOf(false) }

    var minutes by rememberSaveable { mutableIntStateOf(exercise.time.first) }
    var seconds by rememberSaveable { mutableIntStateOf(exercise.time.second) }
    var milliseconds by rememberSaveable { mutableIntStateOf(0) }
    var startTime by rememberSaveable { mutableLongStateOf(0L) }
    var timeLeft by rememberSaveable { mutableLongStateOf(minutes * 60 * 1000L + seconds * 1000L) }

    LaunchedEffect(startButtonState, pauseButtonState) {
        if (startButtonState && !pauseButtonState) {
            startTime = System.currentTimeMillis()
            val endTime = startTime + timeLeft

            while (timeLeft > 0) {
                delay(10L)
                val currentTime = System.currentTimeMillis()
                timeLeft = maxOf(0, endTime - currentTime)

                minutes = (timeLeft / 60000).toInt()
                seconds = ((timeLeft % 60000) / 1000).toInt()
                milliseconds = ((timeLeft % 1000) / 10).toInt()

                if (timeLeft == 0L) {
                    exerciseState = true
                    startButtonState = false
                }
            }
        } else if (!startButtonState && !pauseButtonState) {
            startButtonState = false
            pauseButtonState = false
            minutes = exercise.time.first
            seconds = exercise.time.second
            milliseconds = 0
            timeLeft = (minutes * 60 * 1000L + seconds * 1000L)
        }
    }

    Column(
        modifier = modifier.padding(top = standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format(
                Locale.US,
                "%02d : %02d : %02d",
                minutes,
                seconds,
                milliseconds
            ),
            color = if (!exerciseState) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.primary
            },
            style = MaterialTheme.typography.displayLarge
        )

        Row(horizontalArrangement = Arrangement.spacedBy(standardPadding)) {
            if (!exerciseState) {
                Button(
                    onClick = {
                        if (!startButtonState) {
                            startButtonState = true
                        } else {
                            pauseButtonState = !pauseButtonState
                        }
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            !startButtonState -> MaterialTheme.colorScheme.primary
                            !pauseButtonState -> Color(0xFFFFAB00)
                            else -> MaterialTheme.colorScheme.primary
                        },
                        contentColor = MaterialTheme.colorScheme.scrim
                    )
                ) {
                    Text(
                        text = stringResource(
                            if (!startButtonState) R.string.start
                            else if (!pauseButtonState) R.string.pause
                            else R.string.resume
                        ),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.congratulations_you_have_completed),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            if (startButtonState) {
                Button(
                    onClick = {
                        startButtonState = false
                        pauseButtonState = false
                        minutes = exercise.time.first
                        seconds = exercise.time.second
                        milliseconds = 0
                        timeLeft = (minutes * 60 * 1000L + seconds * 1000L)
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text(
                        text = stringResource(R.string.stop),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding),
            ) {
                Text(
                    text = stringResource(R.string.guide),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = exercise.guide,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding),
            ) {
                Text(
                    text = stringResource(R.string.detail),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "${stringResource(R.string.time)}:" +
                            if (exercise.time.first != 0) {
                                " ${exercise.time.first}min"
                            } else {
                                " "
                            } +
                            if (exercise.time.second != 0) {
                                " ${exercise.time.second}sec"
                            } else {
                                ""
                            },
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "${stringResource(R.string.calories)}: ${exercise.calories}${
                        stringResource(
                            R.string.kcal
                        )
                    }",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "${stringResource(R.string.intensity)}: ${stringResource(exercise.intensity.resId)}",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding),
            ) {
                Text(
                    text = stringResource(R.string.effect),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = exercise.effect,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
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
private fun ExerciseViewScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ExerciseViewScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun ExerciseViewScreenPreviewInLargePhone() {
    BioFitTheme {
        ExerciseViewScreen()
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
private fun ExerciseViewScreenPreviewInTablet() {
    BioFitTheme {
        ExerciseViewScreen()
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
private fun ExerciseViewScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ExerciseViewScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ExerciseViewScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        ExerciseViewScreen()
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
private fun ExerciseViewScreenLandscapePreviewInTablet() {
    BioFitTheme {
        ExerciseViewScreen()
    }
}