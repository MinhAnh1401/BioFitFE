package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class InfoUserBirthdayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                InfoUserBirthdayScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun InfoUserBirthdayScreen() {
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
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.secondary
                                ),
                                screenWidth = screenWidth
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            InfoUserBirthdayContent(screenWidth)
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun InfoUserBirthdayContent(screenWidth: Int) {
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

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
                    text = stringResource(id = R.string.what_is_your_date_of_birth),
                    modifier = Modifier.padding(vertical = 32.dp),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall
                )
            }

            item {
                Text(
                    text = stringResource(R.string.description_date_of_birth),
                    modifier = Modifier.padding(bottom = 32.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            item {
                OutlinedTextField(
                    value = day,
                    onValueChange = { day = it },
                    modifier = if (screenWidth > 600) {
                        Modifier.width(screenWidth.dp * 0.6f)
                    } else {
                        Modifier.fillMaxWidth()
                    },
                    textStyle = MaterialTheme.typography.bodySmall,
                    label = {
                        Text(
                            text = stringResource(R.string.day),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    shape = MaterialTheme.shapes.large
                )

                OutlinedTextField(
                    value = month,
                    onValueChange = { month = it },
                    modifier = if (screenWidth > 600) {
                        Modifier.width(screenWidth.dp * 0.6f)
                    } else {
                        Modifier.fillMaxWidth()
                    }.padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                    label = {
                        Text(
                            text = stringResource(R.string.month),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    shape = MaterialTheme.shapes.large
                )

                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    modifier = if (screenWidth > 600) {
                        Modifier.width(screenWidth.dp * 0.6f)
                    } else {
                        Modifier.fillMaxWidth()
                    }.padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                    label = {
                        Text(
                            text = stringResource(R.string.year),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    shape = MaterialTheme.shapes.large
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
private fun InfoUserBirthdayScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserBirthdayScreen()
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
private fun InfoUserBirthdayScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserBirthdayScreen()
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
private fun InfoUserBirthdayScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserBirthdayScreen()
    }
}