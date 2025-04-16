package com.example.biofit.data.domain

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.biofit.view_model.NotificationViewModel
import com.example.biofit.view_model.NotificationWorker
import com.example.biofit.view_model.WebSocketService
import java.util.concurrent.TimeUnit

class BioFit : Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean("is_first_time", true)

        if (isFirstTime) {
            val viewModel = NotificationViewModel(applicationContext.toString())
            viewModel.sendWelcomeNotification()
            sharedPref.edit().putBoolean("is_first_time", false).apply()}
        setupNotificationWork()
        createNotificationChannel()

        // Khởi động WebSocketService
        val serviceIntent = Intent(this, WebSocketService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun setupNotificationWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            1, TimeUnit.HOURS // Kiểm tra mỗi giờ
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "meal_notification_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "meal_channel",
                "Meal Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for meal times"
            }

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}