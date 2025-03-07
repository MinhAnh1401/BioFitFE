package com.example.biofit.ui.activity

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.model.dto.UserDTO
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.DailyLogViewModel

class UpdateWeightActivity : ComponentActivity() {
    private var userData: UserDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userData = UserSharedPrefsHelper.getUserData(this)
        setContent {
            BioFitTheme {
                UpdateWeightScreen(userData ?: UserDTO.default())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun UpdateWeightScreen(
    userData: UserDTO,
    dailyLogViewModel: DailyLogViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                            start = standardPadding,
                            end = standardPadding,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopBar(
                        onBackClick = { activity?.finish() },
                        title = stringResource(R.string.update_weight),
                        middleButton = null,
                        rightButton = null,
                        standardPadding = standardPadding
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UpdateWeightContent(
                            userData,
                            standardPadding,
                            modifier
                        )
                    }

                    ElevatedButton(
                        onClick = {
                            dailyLogViewModel.updateUserId(userData.userId)
                            dailyLogViewModel.saveDailyLog(context)
                            Toast.makeText(
                                context,
                                R.string.update_daily_weight_successfully,
                                Toast.LENGTH_SHORT
                            ).show()
                            activity?.finish()
                        },
                        modifier = Modifier.widthIn(min = standardPadding * 10),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(
                        modifier = Modifier.padding(
                            bottom = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateBottomPadding() * 2
                                    + standardPadding
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun UpdateWeightContent(
    userData: UserDTO,
    standardPadding: Dp,
    modifier: Modifier,
    dailyLogViewModel: DailyLogViewModel = viewModel()
) {
    val context = LocalContext.current
    val targetWeight = userData.targetWeight
    LaunchedEffect(userData.userId) {
        dailyLogViewModel.updateUserId(userData.userId)
        dailyLogViewModel.getLatestDailyLog(context)
    }
    val memoryWeight by produceState(initialValue = 0f, key1 = dailyLogViewModel.memoryWeight) {
        value = dailyLogViewModel.memoryWeight.value
    }
    LaunchedEffect(Unit) {
        if (dailyLogViewModel.weight.value == null) {
            dailyLogViewModel.weight.value = memoryWeight
        }
    }

    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier.padding(top = standardPadding),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.current_weight),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = stringResource(R.string.target_weight) + " $targetWeight" +
                        stringResource(R.string.kg),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val weight = dailyLogViewModel.weight.value ?: memoryWeight
                    if (weight > 0) {
                        dailyLogViewModel.weight.value = weight - 1f
                    }
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_less_update_weight),
                    contentDescription = stringResource(R.string.current_weight),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            OutlinedTextField(
                value = dailyLogViewModel.weight.value.toString(),
                onValueChange = { input ->
                    dailyLogViewModel.weight.value = input.toFloatOrNull() ?: 0f
                },
                modifier = Modifier
                    .widthIn(min = 10.dp, max = standardPadding * 10)
                    .width(IntrinsicSize.Min),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                suffix = { Text(text = stringResource(R.string.kg)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                shape = MaterialTheme.shapes.large
            )

            IconButton(
                onClick = {
                    val weight = dailyLogViewModel.weight.value ?: memoryWeight
                    dailyLogViewModel.weight.value = weight + 1f
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_update_weight),
                    contentDescription = stringResource(R.string.current_weight),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
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
private fun UpdateWeightScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        UpdateWeightScreen(UserDTO.default())
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun UpdateWeightScreenPreviewInLargePhone() {
    BioFitTheme {
        UpdateWeightScreen(UserDTO.default())
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
private fun UpdateWeightScreenPreviewInTablet() {
    BioFitTheme {
        UpdateWeightScreen(UserDTO.default())
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
private fun UpdateWeightScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        UpdateWeightScreen(UserDTO.default())
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun UpdateWeightScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        UpdateWeightScreen(UserDTO.default())
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
private fun UpdateWeightScreenLandscapePreviewInTablet() {
    BioFitTheme {
        UpdateWeightScreen(UserDTO.default())
    }
}