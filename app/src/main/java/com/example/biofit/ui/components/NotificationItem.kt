package com.example.biofit.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NotificationItem(
    onClick: () -> Unit,
    notification: String,
    standardPadding: Dp
) {
    val currentDateTime = LocalDateTime.now()
    // Thay thành điều kiện khác nếu cần thiết
    val icon = if (currentDateTime.hour < 12) {
        Pair(R.drawable.cloud_sun_fill, Color(0xFFFFAB00))
    } else if (currentDateTime.hour < 18) {
        Pair(R.drawable.sun_max_fill, Color(0xFFDD2C00))
    } else {
        Pair(R.drawable.cloud_moon_fill, Color(0xFF2962FF))
    }

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)), // nếu chưa đọc thì sẽ có màu nền này
    ) {
        Row(
            modifier = Modifier.padding(standardPadding),
            horizontalArrangement = Arrangement.spacedBy(standardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon.first),
                contentDescription = "Notification Icon",
                modifier = Modifier.size(standardPadding * 3f),
                tint = icon.second
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
private fun NotificationItemDarkModePreview() {
    BioFitTheme {
        NotificationItem(
            onClick = {},
            notification = "Notification 1",
            standardPadding = getStandardPadding().first
        )
    }
}

@Preview
@Composable
private fun NotificationItemPreview() {
    BioFitTheme {
        NotificationItem(
            onClick = {},
            notification = "Notification 1",
            standardPadding = getStandardPadding().first
        )
    }
}