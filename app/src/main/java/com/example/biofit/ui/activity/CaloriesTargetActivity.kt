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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

class CaloriesTargetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                CaloriesTargetScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun CaloriesTargetScreen() {
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
                onHomeClick = {
                    activity?.let {
                        val intent = Intent(it, MainActivity::class.java)
                        it.startActivity(intent)
                    }
                },
                title = stringResource(R.string.calories_target),
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

            CaloriesTargetContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun CaloriesTargetContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    var calorieIntake by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
    ) {
        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.daily_calorie_intake),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                TextField(
                    value = calorieIntake,
                    onValueChange = { calorieIntake = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.calorie_intake),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
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
            val percentageProtein = rememberSaveable { mutableStateOf("") }
            val percentageCarbs = rememberSaveable { mutableStateOf("") }
            val percentageFat = rememberSaveable { mutableStateOf("") }
            val listMacronutrientBalanceTextFields = listOf(
                Pair(percentageProtein, R.string.protein),
                Pair(percentageCarbs, R.string.carbohydrate),
                Pair(percentageFat, R.string.fat)
            )

            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.macronutrient_balance),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                listMacronutrientBalanceTextFields.forEach { (percentage, title) ->
                    TextField(
                        value = percentage.value,
                        onValueChange = { percentage.value = it },
                        modifier = modifier,
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.End
                        ),
                        prefix = {
                            Text(
                                text = stringResource(title),
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
                }
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.total_macro_percentage),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.macro_ratio_will_always_be_100),
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "100%",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
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
private fun CaloriesTargetScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CaloriesTargetScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun CaloriesTargetScreenPreviewInLargePhone() {
    BioFitTheme {
        CaloriesTargetScreen()
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
private fun CaloriesTargetScreenPreviewInTablet() {
    BioFitTheme {
        CaloriesTargetScreen()
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
private fun CaloriesTargetScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CaloriesTargetScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CaloriesTargetScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        CaloriesTargetScreen()
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
private fun CaloriesTargetScreenLandscapePreviewInTablet() {
    BioFitTheme {
        CaloriesTargetScreen()
    }
}