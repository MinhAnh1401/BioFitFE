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

class InfoUserHeightAndWeightActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                InfoUserHeightAndWeightScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun InfoUserHeightAndWeightScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            BackgroundInfoScreen()
            NextButtonInfoScreen(onClick = { /* TODO */ })
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.safeDrawing.asPaddingValues())
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBarInfoScreen(
                    onBackClick = { /* TODO */ },
                    stepColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                InfoUserHeightAndWeightContent()
            }
        }
    }
}

@Composable
fun InfoUserHeightAndWeightContent() {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(WindowInsets.ime.asPaddingValues()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Text(
                text = stringResource(R.string.what_is_your_height_and_weight),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(vertical = 32.dp),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = stringResource(R.string.description_height_and_weight),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 32.dp),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }

        item {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                    Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
                } else {
                    Modifier.fillMaxWidth()
                },
                textStyle = MaterialTheme.typography.bodySmall,
                label = {
                    Text(
                        stringResource(R.string.height),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                suffix = {
                    Text(
                        stringResource(R.string.cm),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                isError = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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

        item {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                    Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
                } else {
                    Modifier.fillMaxWidth()
                }.padding(top = 8.dp),
                textStyle = MaterialTheme.typography.bodySmall,
                label = {
                    Text(
                        stringResource(R.string.weight),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                suffix = {
                    Text(
                        stringResource(R.string.kg),
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                isError = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserHeightAndWeightScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserHeightAndWeightScreen()
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
private fun InfoUserHeightAndWeightScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserHeightAndWeightScreen()
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
private fun InfoUserHeightAndWeightScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserHeightAndWeightScreen()
    }
}