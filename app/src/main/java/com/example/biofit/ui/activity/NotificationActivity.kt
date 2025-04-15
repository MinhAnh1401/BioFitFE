package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.biofit.R
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.data.model.dto.MealType
import com.example.biofit.data.model.dto.NotificationDTO
import com.example.biofit.ui.theme.Typography
import com.example.biofit.view_model.NotificationViewModel
import com.example.biofit.view_model.NotificationViewModelFactory
import java.time.format.DateTimeFormatter

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Lấy userId từ SharedPreferences hoặc nơi lưu trữ khác
        val userId = UserSharedPrefsHelper.getUserId(this).toString()
        Log.d("TAG", "User ID: $userId")

        if (userId == "0") {
            Toast.makeText(this, "Vui lòng đăng nhập để xem thông báo", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContent {
            BioFitTheme {
                // Truyền userId vào ViewModel
                val viewModel: NotificationViewModel = viewModel(
                    factory = NotificationViewModelFactory(userId)
                )

                // Load notifications khi khởi tạo
                LaunchedEffect(Unit) {
                    viewModel.loadNotifications()
                }
                NotificationScreen(
                    userId = userId,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    userId: String,
    modifier: Modifier = Modifier
) {
    val viewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(userId)
    )

    // Refresh when composable is recomposed
    LaunchedEffect(Unit) {
        viewModel.refreshIfNeeded()
    }

    // Collect states from ViewModel
    val notificationDTO = viewModel.notifications.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    // Load notifications when screen is displayed
    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(R.string.notification),
                middleButton = null,
                rightButton = {
                    if (notificationDTO.any { !it.isRead }) {
                        IconButton(
                            onClick = { viewModel.markAllAsRead() },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_tick),
                                contentDescription = "Mark All as Read",
                                modifier = Modifier.size(standardPadding * 1.5f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                standardPadding = standardPadding
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (notificationDTO.isEmpty()) {
                EmptyNotificationView()
            } else {
                NotificationListView(
                    notifications = notificationDTO,
                    onMarkAsRead = { notificationId -> viewModel.markAsRead(notificationId = notificationId.toLong()) },
                    onDelete = { notificationId -> viewModel.deleteNotification(notificationId = notificationId.toLong()) },
                    onMarkAllAsRead = { viewModel.markAllAsRead()},
                    onDeleteAll = { viewModel.deleteAllNotifications(userId)}
                )
            }
        }
    }
}

@Composable
fun EmptyNotificationView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "No Notifications",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_notifications),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.you_ll_see_your_notifications_here_when_you_get_them),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationListView(
    notifications: List<NotificationDTO>,
    onMarkAsRead: (Long) -> Unit,
    onDelete: (String) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onDeleteAll: () -> Unit

) {

    Column {
        // Header với nút điều khiển
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (notifications.isNotEmpty()) {
                // Nút "Delete All" khi có thông báo nhiều
                IconButton(
                    onClick = {
                        onDeleteAll()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash_circle),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "Delete All Notifications",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(
                items = notifications,
                key = { index, item -> item.id }
            ) { index, notification ->
                NotificationItem(
                    notification = notification,
                    onMarkAsRead = { onMarkAsRead(notification.id) },
                    onDelete = { onDelete(notification.id.toString()) },
                    modifier = Modifier.animateItemPlacement(tween(300))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationItem(
    notification: NotificationDTO,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible = remember { mutableStateOf(true).value }

//    val isRead = remember { mutableStateOf(notification.isRead) }
//
//    LaunchedEffect(notification.isRead) {
//        isRead.value = notification.isRead
//    }

    // Trạng thái cho swipe
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                isVisible = false
                onDelete()
                true
            } else {
                false // Ngăn xóa khi vuốt sang phải
            }
        }
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart), // Chỉ cho phép vuốt sang trái
            background = {
                // Nền khi vuốt sang trái
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                }
            },
            dismissContent = {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { if (!notification.isRead) onMarkAsRead() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (notification.isRead)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Notification Icon based on MealType
                        NotificationIcon(notification.mealType)

                        Spacer(modifier = Modifier.width(12.dp))

                        // Notification Content
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = notification.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = notification.message,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = notification.scheduledTime?.format(
                                    DateTimeFormatter.ofPattern("MMM dd,yyyy HH:mm:ss")
                                ) ?: "Unknown time",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Trạng thái đọc
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (!notification.isRead)
                                        MaterialTheme.colorScheme.onError
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun NotificationIcon(mealType: MealType) {
    val emoji = when (mealType) {
        MealType.BREAKFAST -> painterResource(R.drawable.frying_pan_fill)
        MealType.LUNCH -> painterResource(R.drawable.basket_fill)
        MealType.DINNER -> painterResource(R.drawable.fork_knife)
        MealType.SNACK -> painterResource(R.drawable.popcorn_fill)
        MealType.SLEEP -> painterResource(R.drawable.bed_double_fill)
        MealType.OTHER -> painterResource(R.drawable.horn_blast_fill)
    }

    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = emoji,
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = when (mealType) {
                MealType.BREAKFAST -> Color(0xFFFFAB00)
                MealType.LUNCH -> Color(0xFFDD2C00)
                MealType.DINNER -> Color(0xFF2962FF)
                MealType.SNACK -> Color(0xFF00BFA5)
                MealType.SLEEP -> Color(0xFF6200EA)
                MealType.OTHER -> MaterialTheme.colorScheme.primary
            },
        )
    }
}

@Composable
fun BioFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFFF5F5F5),
            surface = Color.White,
            surfaceVariant = Color(0xFFE8E8E8)
        ),
        typography = Typography,
        content = content
    )
}
