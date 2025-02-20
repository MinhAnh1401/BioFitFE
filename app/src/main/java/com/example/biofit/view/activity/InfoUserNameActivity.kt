package com.example.biofit.view.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.view.theme.BioFitTheme

class InfoUserNameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                InfoUserNameScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun InfoUserNameScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box {
            BackgroundInfoScreen()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                        start = standardPadding,
                        end = standardPadding,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopBarInfoScreen(
                    onBackClick = { activity?.finish() },
                    stepColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary
                    ),
                    screenWidth,
                    screenHeight,
                    standardPadding
                )

                InfoUserNameContent(
                    standardPadding,
                    modifier
                )
            }

            NextButtonInfoScreen(
                onClick = {
                    activity?.let {
                        val intent = Intent(it, InfoUserGenderActivity::class.java)
                        it.startActivity(intent)
                        it.finish()
                    }
                },
                standardPadding
            )
        }
    }
}

@Composable
fun BackgroundInfoScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_info_screen),
            contentDescription = "Information Screen Background",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun NextButtonInfoScreen(
    onClick: () -> Unit,
    standardPadding: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(standardPadding * 2),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(standardPadding * 4)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.btn_next),
                contentDescription = "Next Button",
                modifier = Modifier.size(standardPadding * 4),
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
fun TopBarInfoScreen(
    onBackClick: () -> Unit,
    stepColors: List<androidx.compose.ui.graphics.Color>,
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BackButton(
            onBackClick,
            standardPadding
        )
        Spacer(modifier = Modifier.width(standardPadding))
        ProgressIndicatorTopBarInfoScreen(
            stepColors,
            screenWidth,
            screenHeight,
            standardPadding
        )
    }
}

@Composable
fun BackButton(
    onBackClick: () -> Unit = {},
    standardPadding: Dp
) {
    IconButton(
        onClick = onBackClick,
        modifier = Modifier.size(standardPadding * 2),
        enabled = true,
    ) {
        Image(
            painter = painterResource(id = R.drawable.btn_back),
            contentDescription = "Back Button",
            modifier = Modifier.size(standardPadding * 2),
        )
    }
}

@Composable
fun ProgressIndicatorTopBarInfoScreen(
    stepColors: List<androidx.compose.ui.graphics.Color>,
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = standardPadding * 2),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        stepColors.forEach { color ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(
                        ratio = 1f / (
                                standardPadding.value /
                                        if (screenWidth > screenHeight || screenWidth > 450) {
                                            250
                                        } else {
                                            100
                                        }
                                )
                    )
                    .clip(MaterialTheme.shapes.large)
                    .background(color = color)
            )
            Spacer(modifier = Modifier.width(standardPadding))
        }
    }
}

@Composable
fun InfoUserNameContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    var username by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = stringResource(R.string.what_is_your_name),
                modifier = Modifier.padding(
                    top = standardPadding * 3,
                    bottom = standardPadding
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall
            )
        }

        item {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = modifier,
                textStyle = MaterialTheme.typography.bodySmall,
                label = {
                    Text(
                        text = stringResource(R.string.full_name),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(
                    onDone = { /*TODO*/ },
                    onGo = { /*TODO*/ },
                    onNext = { /*TODO*/ },
                    onPrevious = { /*TODO*/ },
                    onSearch = { /*TODO*/ },
                    onSend = { /*TODO*/ }
                ),
                singleLine = true,
                maxLines = 1,
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
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
private fun InfoUserNamePortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserNameScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserNamePortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserNameScreen()
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
private fun InfoUserNamePortraitScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserNameScreen()
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
private fun InfoUserNameLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserNameScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserNameLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserNameScreen()
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
private fun InfoUserNameLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserNameScreen()
    }
}