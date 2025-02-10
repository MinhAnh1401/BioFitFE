package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f
    val modifier = if (screenWidth > screenHeight) {
        Modifier.width(((screenWidth + screenHeight) / 2).dp)
    } else {
        Modifier.fillMaxWidth()
    }

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
            TopBarSetting(
                onBackClick = { TODO() }, // Xử lý sự kiện khi người dùng nhấn nút Back
                title = R.string.notification,
                middleButton = null,
                rightButton = {
                    IconButton(
                        onClick = { TODO() }, // Xử lý sự kiện khi người dùng nhấn tick
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_tick),
                            contentDescription = "Tick Button",
                            modifier = Modifier.size(standardPadding * 2)
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
    } else{
        LazyColumn {
            item {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(standardPadding / 10)
                ) {
                    notifications.forEach { notification ->
                        NotificationItem(
                            notification,
                            standardPadding
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: String,
    standardPadding: Dp
) {
    val currentDateTime = LocalDateTime.now()
    // Thay thành điều kiện khác nếu cần thiết
    val icon = if (currentDateTime.hour < 12){
        R.drawable.ic_morning
    } else if (currentDateTime.hour < 18){
        R.drawable.ic_afternoon
    } else {
        R.drawable.ic_evening
    }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)), // nếu đã đọc thì sẽ có màu nền này
    ) {
        Row(
            modifier = Modifier.padding(standardPadding),
            horizontalArrangement = Arrangement.spacedBy(standardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "Notification Icon"
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(standardPadding / 2)
            ) {
                Text(
                    text = notification,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleSmall,
                )

                Text(
                    text = currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
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
private fun DarkModePreviewInSmallPhone() {
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
private fun PreviewInLargePhone() {
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
private fun PreviewInTablet() {
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
private fun LandscapeDarkModePreviewInSmallPhone() {
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
private fun LandscapePreviewInLargePhone() {
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
private fun LandscapePreviewInTablet() {
    BioFitTheme {
        NotificationScreen()
    }
}