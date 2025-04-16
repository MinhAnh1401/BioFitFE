package com.example.biofit.view_model

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.biofit.R
import com.example.biofit.data.model.dto.MealType
import com.example.biofit.data.model.dto.NotificationDTO
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.activity.NotificationActivity
import io.reactivex.disposables.CompositeDisposable
import ua.naiksoftware.stomp.dto.LifecycleEvent
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

class WebSocketService : Service() {

    private lateinit var stompClient: StompClient
    private val disposables = CompositeDisposable()
    private val CHANNEL_ID = "biofit_notification_channel"

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildForegroundNotification())

        // Khởi tạo WebSocket
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://localhost:3306/ws/websocket")
        connectWebSocket()
    }

    private fun connectWebSocket() {
        // Connect to the WebSocket using the lifecycle methods
        val connectLifecycle = stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.d("WebSocketService", "Connected")
                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.e("WebSocketService", "Connection error: ${lifecycleEvent.exception?.message}")
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        Log.d("WebSocketService", "Connection closed")
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        Log.w("WebSocketService", "Failed server heartbeat")
                    }
                    else -> {
                        Log.d("WebSocketService", "Other lifecycle event: ${lifecycleEvent.type}")
                    }
                }
            }
        disposables.add(connectLifecycle)

        // Initiate the connection
        stompClient.connect()

        val userId = UserSharedPrefsHelper.getUserId(this).toString()
        val topicSubscription = stompClient.topic("/topic/notifications/$userId")
            .subscribeOn(Schedulers.io())
            .subscribe(
                { message ->
                    val notificationDTO = parseNotificationDTO(message.payload)
                    showSystemNotification(notificationDTO.title, notificationDTO.message)
                },
                { error ->
                    Log.e("WebSocketService", "Subscription error: ${error.message}")
                }
            )
        disposables.add(topicSubscription)
    }


    private fun parseNotificationDTO(payload: String): NotificationDTO {
        // Parse JSON thủ công hoặc dùng Gson
        val json = JSONObject(payload)
        return NotificationDTO(
            id = json.getLong("id"),
            title = json.getString("title"),
            message = json.getString("message"),
            mealType = MealType.valueOf(json.getString("mealType")),
            scheduledTime = json.getString("scheduledTime"),
            createdAt = null,
            isReminderSent = false,
            isRead = json.getBoolean("isRead")
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "BioFit Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for BioFit notifications"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildForegroundNotification() =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("BioFit đang chạy")
            .setContentText("Đang nhận thông báo khung giờ")
            .setSmallIcon(R.drawable.bell_fill) // Thay bằng icon của bạn
            .build()

    private fun showSystemNotification(title: String, message: String) {
        val intent = Intent(this, NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell_fill) // Thay bằng icon của bạn
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        stompClient.disconnect()
        disposables.clear()
        super.onDestroy()
    }
}