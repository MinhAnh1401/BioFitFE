package com.example.biofit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.example.biofit.ui.theme.BioFitTheme

@Composable
fun ToggleButtonBar(
    options: List<Int>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    standardPadding: Dp
) {
    SubCard(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier.padding(standardPadding / 6),
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
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold.takeIf { isSelected }
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ToggleButtonBarScreenDarkModePreview() {
    BioFitTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(getStandardPadding().first)) {
            ToggleButtonBar(
                options = listOf(
                    R.string.morning,
                    R.string.afternoon,
                    R.string.evening,
                    R.string.snack
                ),
                selectedOption = R.string.evening,
                onOptionSelected = { },
                standardPadding = getStandardPadding().first
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToggleButtonBarScreenPreview() {
    BioFitTheme {
        Box(modifier = Modifier.padding(getStandardPadding().first)) {
            ToggleButtonBar(
                options = listOf(R.string.day, R.string.week),
                selectedOption = R.string.day,
                onOptionSelected = { },
                standardPadding = getStandardPadding().first
            )
        }
    }
}