package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.NotificationItem
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                NotificationScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun NotificationScreen() {
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
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                ),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(R.string.notification),
                middleButton = null,
                rightButton = {
                    IconButton(
                        onClick = { TODO() }, // Xử lý sự kiện khi người dùng nhấn tick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tick),
                            contentDescription = "Tick Button",
                            modifier = Modifier.size(standardPadding * 1.5f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                standardPadding = standardPadding
            )

            NotificationContent(
                standardPadding,
                modifier
            )
        }
    }
}

@Composable
fun NotificationContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val notifications = listOf(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 1",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 2",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 3",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 4",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 5",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 6",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 7",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 8",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 9",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry 10"
    ) // Danh sách thông báo

    if (notifications.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You have no notifications",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    } else {
        LazyColumn {
            item {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(standardPadding / 10)
                ) {
                    notifications.forEach { notification ->
                        NotificationItem(
                            onClick = { TODO() },
                            notification = notification,
                            standardPadding = standardPadding
                        )
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier.padding(
                        bottom = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateBottomPadding() * 2
                                + standardPadding
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
private fun NotificationScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        NotificationScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun NotificationScreenPreviewInLargePhone() {
    BioFitTheme {
        NotificationScreen()
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
private fun NotificationScreenPreviewInTablet() {
    BioFitTheme {
        NotificationScreen()
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
private fun NotificationScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        NotificationScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun NotificationScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        NotificationScreen()
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
private fun NotificationScreenLandscapePreviewInTablet() {
    BioFitTheme {
        NotificationScreen()
    }
}