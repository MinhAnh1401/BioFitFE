package com.example.biofit.view.dialog

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.view.activity.getStandardPadding
import com.example.biofit.view.ui_theme.BioFitTheme

@Composable
fun ToggleButtonBarScreen() {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            ToggleButtonBar(
                options = listOf(
                    R.string.day,
                    R.string.week,
                    R.string.year
                ),
                selectedOption = R.string.day,
                onOptionSelected = {},
                standardPadding = getStandardPadding().first
            )
        }
    }
}

@Composable
fun ToggleButtonBar(
    options: List<Int>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    standardPadding: Dp
) {
    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(standardPadding / 6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = standardPadding / 2),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = option),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
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
private fun ToggleButtonBarScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ToggleButtonBarScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ToggleButtonBarScreenPreviewInLargePhone() {
    BioFitTheme {
        ToggleButtonBarScreen()
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
private fun ToggleButtonBarScreenPreviewInTablet() {
    BioFitTheme {
        ToggleButtonBarScreen()
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
private fun ToggleButtonBarScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ToggleButtonBarScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ToggleButtonBarScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        ToggleButtonBarScreen()
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
private fun ToggleButtonBarScreenLandscapePreviewInTablet() {
    BioFitTheme {
        ToggleButtonBarScreen()
    }
}