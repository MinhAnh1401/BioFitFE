package com.example.biofit.view.dialog

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.view.activity.BackButton
import com.example.biofit.view.activity.HomeButton
import com.example.biofit.view.activity.getStandardPadding
import com.example.biofit.view.ui_theme.BioFitTheme

@Composable
fun TopBarScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TopBar(
            onBackClick = { },
            onHomeClick = { },
            title = "Title",
            gradientTitle = null,
            middleButton = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            rightButton = {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "Add button",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            standardPadding = getStandardPadding().first
        )

        TopBar(
            onBackClick = { },
            onHomeClick = { },
            title = null,
            gradientTitle = "Gradient title",
            middleButton = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            rightButton = {
                Text(
                    text = stringResource(R.string.save),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            },
            standardPadding = getStandardPadding().first
        )
    }
}

@Composable
fun TopBar(
    onBackClick: (() -> Unit)? = null,
    onHomeClick: (() -> Unit)? = null,
    title: String? = null,
    gradientTitle: String? = null,
    middleButton: (@Composable () -> Unit)? = null,
    rightButton: (@Composable () -> Unit)? = null,
    standardPadding: Dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.Start
        ) {
            Row {
                if (onBackClick != null) {
                    BackButton(
                        onBackClick,
                        standardPadding
                    )
                }

                Spacer(modifier = Modifier.width(standardPadding))

                if (onHomeClick != null) {
                    HomeButton(
                        onHomeClick,
                        standardPadding
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                title?.let {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                gradientTitle?.let {
                    Text(
                        text = gradientTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    if (isSystemInDarkTheme()) {
                                        Color(0xFF64DD17)
                                    } else {
                                        Color(0xFFAEEA00)
                                    },
                                    MaterialTheme.colorScheme.primary
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(
                                    Float.POSITIVE_INFINITY,
                                    Float.POSITIVE_INFINITY
                                )
                            )
                        )
                    )
                }

                middleButton?.invoke()
            }
        }

        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.End
        ) {
            rightButton?.invoke()
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
private fun TopBarScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TopBarScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TopBarScreenPreviewInLargePhone() {
    BioFitTheme {
        TopBarScreen()
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
private fun TopBarScreenPreviewInTablet() {
    BioFitTheme {
        TopBarScreen()
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
private fun TopBarScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TopBarScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TopBarScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        TopBarScreen()
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
private fun TopBarScreenLandscapePreviewInTablet() {
    BioFitTheme {
        TopBarScreen()
    }
}