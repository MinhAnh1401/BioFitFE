package com.example.biofit.view_model

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.biofit.data.model.dto.MealType
import com.example.biofit.data.model.dto.NotificationDTO
import java.time.LocalDateTime
import java.time.LocalTime

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Kiểm tra thời gian hiện tại và tạo thông báo nếu cần
        val currentTime = LocalTime.now()
        val currentHour = currentTime.hour

        val viewModel = NotificationViewModel("testUserId") // Replace with actual userId
        viewModel.loadNotifications()
        return Result.success()

        // Kiểm tra các khung giờ bữa ăn
        when {
            // Bữa sáng (7h)
            currentHour == 7 -> {
                createSystemNotification(
                    "Đã đến giờ ăn sáng rồi! 🍳",
                    "Chào buổi sáng! Hãy nạp năng lượng cho ngày mới nhé!",
                    MealType.BREAKFAST
                )
            }
            // Bữa trưa (12h)
            currentHour == 12 -> {
                createSystemNotification(
                    "Đã đến giờ ăn trưa rồi! 🍲",
                    "Hãy nghỉ ngơi và thưởng thức bữa trưa dinh dưỡng nào!",
                    MealType.LUNCH
                )
            }
            // Bữa tối (18h30)
            currentHour == 18 && currentTime.minute >= 30 -> {
                createSystemNotification(
                    "Đã đến giờ ăn tối rồi! 🍗",
                    "Hãy thưởng thức bữa tối ngon miệng và bổ dưỡng nhé!",
                    MealType.DINNER
                )
            }
            // Giờ ăn nhẹ buổi sáng (10h)
            currentHour == 10 -> {
                createSystemNotification(
                    "Giờ ăn nhẹ buổi sáng 🍎",
                    "Một chút hoa quả hoặc hạt sẽ giúp bạn duy trì năng lượng đến bữa trưa!",
                    MealType.SNACK
                )
            }
            // Giờ ăn nhẹ buổi chiều (15h)
            currentHour == 15 -> {
                createSystemNotification(
                    "Giờ ăn nhẹ buổi chiều 🥜",
                    "Hãy nạp chút năng lượng để hoàn thành tốt công việc cuối ngày!",
                    MealType.SNACK
                )
            }
            // Giờ đi ngủ (21h30)
            currentHour == 22 -> {
                createSystemNotification(
                    "Đã đến giờ đi ngủ rồi! 😴",
                    "Hãy có một giấc ngủ ngon để nạp đầy năng lượng cho ngày mai nhé!",
                    MealType.SNACK
                )
            }
        }

        return Result.success()
    }

    private fun createSystemNotification(title: String, message: String, mealType: MealType) {

        // Lưu notification vào database
        val notification = NotificationDTO(
            id = 0,
            title = title,
            message = message,
            mealType = mealType,
            scheduledTime = LocalDateTime.now().toString(),
            isRead = false,
            createdAt = LocalDateTime.now().toString(),
            isReminderSent = false
        )

        // Lưu notification (sẽ cần injection hoặc singleton để truy cập repository)
        saveNotificationToDatabase(notification)
    }

    private fun saveNotificationToDatabase(notification: NotificationDTO) {
        // Code lưu notification vào database
    }
}