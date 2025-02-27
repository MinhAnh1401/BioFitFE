package com.example.biofit.view.sub_components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.view.ui_theme.BioFitTheme
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
        R.drawable.ic_morning
    } else if (currentDateTime.hour < 18) {
        R.drawable.ic_afternoon
    } else {
        R.drawable.ic_evening
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