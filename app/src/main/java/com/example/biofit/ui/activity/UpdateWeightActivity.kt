package com.example.biofit.ui.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import java.util.Calendar

class UpdateWeightActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                UpdateWeightScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun UpdateWeightScreen() {
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
                        onHomeClick = {
                            activity?.let {
                                val intent = Intent(it, MainActivity::class.java)
                                it.startActivity(intent)
                            }
                        },
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
                            standardPadding,
                            modifier
                        )
                    }

                    ElevatedButton(
                        onClick = { activity?.finish() }, // Xử lý sự kiện khi người dùng nhấn nút Save
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
    standardPadding: Dp,
    modifier: Modifier
) {
    val targetWeight = 70f // Thay đổi giá trị mục tiêu cân nặng tại đây
    var currentWeight by rememberSaveable { mutableStateOf("70") } // Thay đổi giá trị cân nặng hiện tại tại đây
    var weighingDay by rememberSaveable { mutableStateOf("dd/mm/yyyy") } // Thay đổi ngày đo cân tại đây
    val focusManager = LocalFocusManager.current


    Column(
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier.padding(top = standardPadding),
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
                    val weight = currentWeight.toFloatOrNull() ?: 0f
                    if (weight > 0) { // Đảm bảo không giảm xuống dưới 0
                        currentWeight = (weight - 1).toString()
                    }
                }, // Xử lý sự kiện khi người dùng nhấn nút "-"
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_less_update_weight),
                    contentDescription = stringResource(R.string.current_weight),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            OutlinedTextField(
                value = currentWeight,
                onValueChange = { currentWeight = it },
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
                    val weight = currentWeight.toFloatOrNull() ?: 0f
                    currentWeight = (weight + 1).toString()
                }, // Xử lý sự kiện khi người dùng nhấn nút "+"
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_update_weight),
                    contentDescription = stringResource(R.string.current_weight),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        var showDatePicker by rememberSaveable { mutableStateOf(false) }

        TextField(
            value = weighingDay,
            onValueChange = { weighingDay = it },
            modifier = modifier,
            readOnly = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.End),
            leadingIcon = {
                Text(
                    text = stringResource(R.string.weighing_day),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { showDatePicker = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.date_of_birth),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        if (showDatePicker) {
            val context = LocalContext.current
            val calendar = Calendar.getInstance()
            LaunchedEffect(Unit) {
                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        weighingDay = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        showDatePicker = false
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
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
        UpdateWeightScreen()
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
        UpdateWeightScreen()
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
        UpdateWeightScreen()
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
        UpdateWeightScreen()
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
        UpdateWeightScreen()
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
        UpdateWeightScreen()
    }
}