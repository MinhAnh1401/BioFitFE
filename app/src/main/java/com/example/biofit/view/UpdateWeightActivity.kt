package com.example.biofit.view

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

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
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f
    val modifier = if (screenWidth > screenHeight) {
        Modifier.width(((screenWidth + screenHeight) / 2).dp)
    } else {
        Modifier.fillMaxWidth()
    }

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
                    TopBarSetting(
                        onBackClick = { TODO() }, // Xử lý sự kiện khi người dùng nhấn nút Back
                        R.string.update_weight,
                        null,
                        null,
                        standardPadding
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

                    Button(
                        onClick = { TODO() }, // Xử lý sự kiện khi người dùng nhấn nút Save}
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(
                        modifier = Modifier.padding(
                            bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() * 2
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
                textStyle = MaterialTheme.typography.bodySmall,
                suffix = {
                    Text(
                        text = stringResource(R.string.kg),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
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
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
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

        TextField(
            value = weighingDay,
            onValueChange = { weighingDay = it },
            modifier = modifier,
            readOnly = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            ),
            leadingIcon = {
                Text(
                    text = stringResource(R.string.weighing_day),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { TODO() } // Xử lý sự kiện khi người dùng nhấn icon DateRange
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.date_of_birth),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
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
private fun PortraitScreenDarkModePreviewInSmallPhone() {
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
private fun PortraitScreenPreviewInLargePhone() {
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
private fun PortraitScreenPreviewInTablet() {
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
private fun LandscapeScreenDarkModePreviewInSmallPhone() {
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
private fun LandscapeScreenPreviewInLargePhone() {
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
private fun LandscapeScreenPreviewInTablet() {
    BioFitTheme {
        UpdateWeightScreen()
    }
}