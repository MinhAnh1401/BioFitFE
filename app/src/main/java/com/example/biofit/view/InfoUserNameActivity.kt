package com.example.biofit.view

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

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
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        content = {
            Box(
                content = {
                    BackgroundInfoScreen()
                    NextButtonInfoScreen(onClick = { /* TODO */ })
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(WindowInsets.safeDrawing.asPaddingValues())
                            .padding(16.dp)
                            .align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            TopBarInfoScreen(
                                onBackClick = { /* TODO */ },
                                stepColors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.secondary
                                ),
                                screenWidth = screenWidth
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            InfoUserNameContent()
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun BackgroundInfoScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        content = {
            Image(
                painter = painterResource(id = R.drawable.bg_info_screen),
                contentDescription = "Information Screen Background",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
        }
    )
}

@Composable
fun NextButtonInfoScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,
        content = {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(48.dp),
                enabled = true,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_next),
                        contentDescription = "Next Button",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            )
        }
    )
}

@Composable
fun TopBarInfoScreen(
    onBackClick: () -> Unit,
    stepColors: List<androidx.compose.ui.graphics.Color>,
    screenWidth: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp),
                enabled = true,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_back),
                        contentDescription = "Back Button",
                        modifier = Modifier.size(26.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            ProgressIndicatorTopBarInfoScreen(
                stepColors,
                screenWidth = screenWidth
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
    )
}

@Composable
fun ProgressIndicatorTopBarInfoScreen(
    stepColors: List<androidx.compose.ui.graphics.Color>,
    screenWidth: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {
            stepColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(
                            ratio = 1f / if (screenWidth > 600) {
                                0.05f
                            } else {
                                0.2f
                            }
                        )
                        .clip(MaterialTheme.shapes.large)
                        .background(color = color)
                )
                Spacer(
                    modifier = Modifier.width(
                        if (screenWidth > 600) {
                            screenWidth.dp * 0.03f
                        } else {
                            screenWidth.dp * 0.05f
                        }
                    )
                )
            }
        }
    )
}

@Composable
fun InfoUserNameContent() {
    var username by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(WindowInsets.ime.asPaddingValues()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            item {
                Text(
                    text = stringResource(R.string.what_is_your_name),
                    modifier = Modifier.padding(vertical = 32.dp),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall
                )
            }

            item {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = if (LocalConfiguration.current.screenWidthDp > 600) {
                        Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                    } else {
                        Modifier.fillMaxWidth()
                    },
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
                )
            }
        }
    )
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserNameScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserNameScreen()
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
private fun InfoUserNameScreenDarkModePreviewInSmallPhone() {
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
private fun InfoUserNameScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserNameScreen()
    }
}