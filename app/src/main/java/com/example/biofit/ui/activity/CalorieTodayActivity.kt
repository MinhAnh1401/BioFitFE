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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.ui.components.SubCard
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

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
    modifier: Modifier
) {
    val consumeAndBurn = listOf(
        Pair(R.string.consume, 300f),
        Pair(R.string.burn, 100f),
    )

    val eatAndExercise = listOf(
        Triple(R.string.eat, 300f, 500f),
        Triple(R.string.exercise, 100f, 200f),
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Column(
                modifier = modifier.padding(top = standardPadding),
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = "dd/mm",
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
                                Text(
                                    text = stringResource(title),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Text(
                                    text = value.toString(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.displaySmall
                                )

                                Text(
                                    text = stringResource(R.string.kcal),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.titleSmall
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
                        Text(
                            text = stringResource(R.string.workout),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = 15.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.displaySmall
                        )

                        Text(
                            text = stringResource(R.string.min),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleSmall
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
                            text = "$value/$target${stringResource(R.string.kcal)}",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    LinearProgressIndicator(
                        progress = { value / target },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(standardPadding / 2),
                        color = if (title == R.string.eat) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
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