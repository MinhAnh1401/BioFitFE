package com.example.biofit.ui.activity

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.ui.components.TopBar2
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

class TOUAndPPActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val title = intent.getStringExtra("title") ?: ""
        val getFrom = intent.getStringExtra("from") ?: ""
        setContent {
            BioFitTheme {
                TOUAndPPScreen(
                    title,
                    getFrom
                )
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun TOUAndPPScreen(
    title: String,
    getFrom: String
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (getFrom) {
                "profile_screen" -> TopBar2(
                    onBackClick = { activity?.finish() },
                    title = when (title) {
                        stringResource(R.string.term_of_use) -> stringResource(R.string.term_of_use)
                        else -> stringResource(R.string.privacy_policy)
                    },
                    middleButton = null,
                    rightButton = null,
                    standardPadding = standardPadding
                )

                else -> TopBar2(
                    onBackClick = { activity?.finish() },
                    title = when (title) {
                        stringResource(R.string.term_of_use) -> stringResource(R.string.term_of_use)
                        else -> stringResource(R.string.privacy_policy)
                    },
                    middleButton = null,
                    rightButton = null,
                    standardPadding = standardPadding
                )
            }

            TOUAndPPContent(
                title = title,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun TOUAndPPContent(
    title: String,
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Column(modifier = modifier) {
                AppLogo(standardPadding = standardPadding)
            }
        }

        item {
            Text(
                text = when (title) {
                    stringResource(R.string.term_of_use) -> stringResource(R.string.term_of_use_content)
                    else -> stringResource(R.string.privacy_policy_content)
                },
                modifier = modifier,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyLarge,
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
private fun TOUAndPPScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TOUAndPPScreen(
            stringResource(R.string.term_of_use),
            "profile_screen"
        )
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TOUAndPPScreenPreviewInLargePhone() {
    BioFitTheme {
        TOUAndPPScreen(
            stringResource(R.string.term_of_use),
            "profile_screen"
        )
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
private fun TOUAndPPScreenPreviewInTablet() {
    BioFitTheme {
        TOUAndPPScreen(
            stringResource(R.string.term_of_use),
            "profile_screen"
        )
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
private fun TOUAndPPLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TOUAndPPScreen(
            stringResource(R.string.term_of_use),
            "profile_screen"
        )
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TOUAndPPLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        TOUAndPPScreen(
            stringResource(R.string.term_of_use),
            "profile_screen"
        )
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
private fun TOUAndPPLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        TOUAndPPScreen(
            stringResource(R.string.term_of_use),
            "profile_screen"
        )
    }
}