package com.example.biofit.ui.activity

import android.app.Activity
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.components.SubCard
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.ExerciseViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalorieTodayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                CalorieTodayScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun CalorieTodayScreen() {
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
                title = stringResource(R.string.calorie),
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            CalorieTodayContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun CalorieTodayContent(
    standardPadding: Dp,
    modifier: Modifier,
    exerciseViewModel: ExerciseViewModel = viewModel()
) {
    val context = LocalContext.current
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val userData = UserSharedPrefsHelper.getUserData(context)
    val userId = userData?.userId ?: 0
    /*
    ________________________________________________________________________________________________
    */
    val consumedCalories = 500f // Calo hấp thụ từ tất cả thức ăn trong ngày hôm nay
    val formatterConsumedCalories = if (consumedCalories % 1 == 0f) {
        consumedCalories.toInt().toString()
    } else {
        String.format(java.util.Locale.US, "%.1f", consumedCalories)
    }
    val targetCalories = when (userData?.gender) {
        0 -> when (userData.getAgeInt(userData.dateOfBirth)) {
            in 0..45 -> 2000f
            else -> 1500f
        }

        1 -> when (userData.getAgeInt(userData.dateOfBirth)) {
            in 0..30 -> 1500f
            else -> 1000f
        }

        else -> 0f
    }
    /*
    ________________________________________________________________________________________________
    */
    // Lấy dữ liệu calo đốt cháy từ exerciseViewModel
    exerciseViewModel.getBurnedCaloriesToday(userId)
    val burnedCalories = exerciseViewModel.burnedCalories.observeAsState(initial = 0f).value
    val targetBurnCalories = when (userData?.gender) {
        0 -> when (userData.getAgeInt(userData.dateOfBirth)) {
            in 0..45 -> 500f
            else -> 400f
        }

        1 -> when (userData.getAgeInt(userData.dateOfBirth)) {
            in 0..30 -> 400f
            else -> 300f
        }

        else -> 0f
    }
    /*
    ________________________________________________________________________________________________
    */
    // Lấy số phút tập luyện từ exerciseViewModel
    exerciseViewModel.getExerciseDoneTimeToday(userId)
    val exerciseDoneTime = exerciseViewModel.exerciseDoneTime.observeAsState(initial = 0f).value
    val formatterExerciseDoneTime = if (exerciseDoneTime % 1 == 0f) {
        exerciseDoneTime.toInt().toString()
    } else {
        String.format(java.util.Locale.US, "%.1f", exerciseDoneTime)
    }
    /*
    ________________________________________________________________________________________________
    */
    // Tập hợp dữ liệu để hiển thị lên các card và thanh tiến trình
    val consumeAndBurn = listOf(
        Pair(R.string.consume, formatterConsumedCalories),
        Pair(R.string.burn, burnedCalories),
    )

    val eatAndExercise = listOf(
        Triple(R.string.eat, consumedCalories, targetCalories),
        Triple(R.string.exercise, burnedCalories, targetBurnCalories),
    )
    /*
    ________________________________________________________________________________________________
    */
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Column(
                modifier = modifier.padding(top = standardPadding),
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = today,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding),
                ) {
                    consumeAndBurn.forEach { (title, value) ->
                        SubCard(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Column(
                                modifier = Modifier.padding(standardPadding * 2),
                                verticalArrangement = Arrangement.spacedBy(standardPadding)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            when (title) {
                                                R.string.consume -> R.drawable.fork_knife
                                                else -> R.drawable.flame_fill
                                            }
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(standardPadding * 2f),
                                        tint = when (title) {
                                            R.string.consume -> MaterialTheme.colorScheme.primary
                                            else -> Color(0xFFDD2C00)
                                        }
                                    )

                                    Text(
                                        text = stringResource(title),
                                        color = when (title) {
                                            R.string.consume -> MaterialTheme.colorScheme.primary
                                            else -> Color(0xFFDD2C00)
                                        },
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }

                                Text(
                                    text = value.toString(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.displaySmall
                                )

                                Text(
                                    text = stringResource(R.string.kcal),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

                SubCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(standardPadding * 2),
                        verticalArrangement = Arrangement.spacedBy(standardPadding)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.figure_strengthtraining_traditional),
                                contentDescription = null,
                                modifier = Modifier.size(standardPadding * 2f),
                                tint = Color(0xFF2962FF)
                            )

                            Text(
                                text = stringResource(R.string.workout),
                                color = Color(0xFF2962FF),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Text(
                            text = formatterExerciseDoneTime,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.displaySmall
                        )

                        Text(
                            text = stringResource(R.string.min),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                eatAndExercise.forEach { (title, value, target) ->
                    val formatterValue = if (value % 1 == 0f) {
                        value.toInt().toString()
                    } else {
                        String.format(java.util.Locale.US, "%.1f", value)
                    }
                    val formatterTarget = if (target % 1 == 0f) {
                        target.toInt().toString()
                    } else {
                        String.format(java.util.Locale.US, "%.1f", target)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding),
                    ) {
                        Text(
                            text = stringResource(title),
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(
                            text = "$formatterValue/$formatterTarget ${stringResource(R.string.kcal)}",
                            color = when (title) {
                                R.string.eat -> MaterialTheme.colorScheme.primary
                                else -> Color(0xFFDD2C00)
                            },
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    LinearProgressIndicator(
                        progress = { value / target },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(standardPadding / 2),
                        color =  when (title) {
                            R.string.eat -> MaterialTheme.colorScheme.primary
                            else -> Color(0xFFDD2C00)
                        },
                        trackColor = when (title) {
                            R.string.eat -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            else -> Color(0xFFDD2C00).copy(alpha = 0.25f)
                        },
                        strokeCap = StrokeCap.Round
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
private fun CalorieTodayScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CalorieTodayScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun CalorieTodayScreenPreviewInLargePhone() {
    BioFitTheme {
        CalorieTodayScreen()
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
private fun CalorieTodayScreenPreviewInTablet() {
    BioFitTheme {
        CalorieTodayScreen()
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
private fun CalorieTodayScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CalorieTodayScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CalorieTodayScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        CalorieTodayScreen()
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
private fun CalorieTodayScreenLandscapePreviewInTablet() {
    BioFitTheme {
        CalorieTodayScreen()
    }
}