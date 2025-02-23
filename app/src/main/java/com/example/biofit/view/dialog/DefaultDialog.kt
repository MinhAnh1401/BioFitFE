package com.example.biofit.view.dialog

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.biofit.R
import com.example.biofit.view.activity.getStandardPadding
import com.example.biofit.view.ui_theme.BioFitTheme
import java.util.Locale

@Composable
fun DefaultScreen() {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DefaultDialog(
            title = R.string.bionix,
            description = R.string.des_app_name,
            actionTextButton = R.string.activity,
            actionTextButtonColor = MaterialTheme.colorScheme.onPrimary,
            actionButtonColor = MaterialTheme.colorScheme.primary,
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
                .clip(MaterialTheme.shapes.large)
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
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onSecondary,
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
        DefaultScreen()
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
        DefaultScreen()
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
        DefaultScreen()
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
        DefaultScreen()
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
        DefaultScreen()
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
        DefaultScreen()
    }
}