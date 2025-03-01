package com.example.biofit.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import java.util.Locale

@Composable
fun DefaultDialogScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DefaultDialog(
            title = R.string.delete_data,
            description = R.string.des_delete_data,
            actionTextButton = R.string.delete,
            actionTextButtonColor = MaterialTheme.colorScheme.onError,
            actionButtonColor = MaterialTheme.colorScheme.error,
            onClickActionButton = { },
            onDismissRequest = { },
            standardPadding = getStandardPadding().first
        )
    }
}

@Composable
fun DefaultDialog(
    title: Int,
    description: Int? = null,
    actionTextButton: Int,
    actionTextButtonColor: Color,
    actionButtonColor: Color,
    onClickActionButton: () -> Unit,
    onDismissRequest: () -> Unit,
    standardPadding: Dp,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    shape = MaterialTheme.shapes.extraLarge
                )
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    vertical = standardPadding * 2,
                    horizontal = standardPadding
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(title),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall
                )
                if (description != null) {
                    Text(
                        text = stringResource(description),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.background,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Button(
                        onClick = onClickActionButton,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = actionButtonColor
                        )
                    ) {
                        Text(
                            text = stringResource(actionTextButton).lowercase().replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            color = actionTextButtonColor,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
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
private fun DefaultDialogDarkModePreviewInSmallPhone() {
    BioFitTheme {
        DefaultDialogScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DefaultDialogPreviewInLargePhone() {
    BioFitTheme {
        DefaultDialogScreen()
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
private fun DefaultDialogPreviewInTablet() {
    BioFitTheme {
        DefaultDialogScreen()
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
private fun DefaultDialogLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        DefaultDialogScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun DefaultDialogLandscapePreviewInLargePhone() {
    BioFitTheme {
        DefaultDialogScreen()
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
private fun DefaultDialogLandscapePreviewInTablet() {
    BioFitTheme {
        DefaultDialogScreen()
    }
}