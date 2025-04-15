package com.example.biofit.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biofit.data.model.dto.MealType
import com.example.biofit.data.model.dto.NotificationDTO
import com.example.biofit.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(private val userId: String) : ViewModel() {

    internal val _notifications = MutableStateFlow<List<NotificationDTO>>(emptyList())
    val notifications: StateFlow<List<NotificationDTO>> = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()


    private val apiService = RetrofitClient.instance

    private var needsRefresh = true


    fun refreshIfNeeded() {
        needsRefresh = true
        loadNotifications()
    }

    fun loadNotifications() {
        if (!needsRefresh) return
        _isLoading.value = true

        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("Notification", "Fetching for userId: $userId")
                val response = apiService.getUserNotifications(userId)
                Log.d("Notification", "Raw response: ${response.raw()}")
                if (response.isSuccessful) {
                    response.body()?.let { notificationDTOs ->
                        Log.d("Notification", "Parsed ${notificationDTOs.size} notifications: $notificationDTOs")
                        _notifications.value = notificationDTOs.map { dto ->
                            NotificationDTO(
                                id = dto.id,
                                title = dto.title,
                                message = dto.message,
                                mealType = MealType.valueOf(dto.mealType.toString()),
                                scheduledTime = dto.scheduledTime,
                                isRead = dto.isRead,
                                createdAt = dto.createdAt
                            )
                        }
                        _unreadCount.value = _notifications.value.count { !it.isRead }
                    } ?: Log.d("Notification", "Response body is null")
                } else {
                    Log.e("Notification", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Notification", "Exception: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteNotification(notificationId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteNotification(notificationId)
                if (response.isSuccessful) {
                    // Cập nhật local state
                    _notifications.value = _notifications.value.filter {
                        it.id != notificationId
                    }
                }
            } catch (e: Exception) {
                // Xử lý lỗi
            }
        }
    }

    fun markAsRead(notificationId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.markAsRead(notificationId)
                if (response.isSuccessful) {
                    // Cập nhật local state
                    _notifications.update { currentNotifications ->
                        currentNotifications.map { notification ->
                            if (notification.id == notificationId) {
                                notification.copy(isRead = true)
                            } else {
                                notification
                            }
                        }
                    }
                } else {
                    // Xử lý lỗi
                    Log.e("Notification", "Failed to mark as read: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Notification", "Error marking as read", e)
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                val unreadIds = _notifications.value
                    .filter { !it.isRead }
                    .map { it.id.toLong() }

                val response = apiService.markAllAsRead(userId, unreadIds)
                if (response.isSuccessful) {
                    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
                    Log.d("Notification", "All notifications marked as read")
                }
            } catch (e: Exception) {
                // Xử lý lỗi
                Log.e("Notification", "Error marking all as read", e)
            }
        }
    }

    fun sendWelcomeNotification() {
        viewModelScope.launch {
            try {
                val response = apiService.sendWelcomeNotification(userId)
                if (response.isSuccessful) {
                    // Thêm vào danh sách hiện tại
                    response.body()?.let { notificationDto ->
                        val welcomeNotification = NotificationDTO(
                            id = notificationDto.id,
                            title = notificationDto.title,
                            message = notificationDto.message,
                            mealType = MealType.OTHER,
                            scheduledTime = notificationDto.scheduledTime,
                            isRead = false,
                            createdAt = notificationDto.createdAt
                        )
                        _notifications.value = listOf(welcomeNotification) + _notifications.value
                    }
                }
            } catch (e: Exception) {
                Log.e("Notification", "Failed to send welcome notification", e)
            }
        }
    }

    fun deleteAllNotifications() {
        viewModelScope.launch {
            try {
                val response = apiService.deleteAllNotifications(userId)
                if (response.isSuccessful) {
                    // Cập nhật local state
                    _notifications.value = _notifications.value.filter {
                        it.id != userId.toLong()
                    }
                }
            } catch (e: Exception) {
                // Xử lý lỗi
                Log.e("Notification", "Error deleting all notifications", e)
            }
        }
    }
}