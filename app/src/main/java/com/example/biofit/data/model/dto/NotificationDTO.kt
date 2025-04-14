package com.example.biofit.data.model.dto

import java.time.LocalDateTime

data class NotificationDTO (
    val id : Long,
    val title: String,
    val message: String,
    val mealType: MealType,
    val scheduledTime: String?,
    val createdAt: String?,
    val isReminderSent: Boolean = false,
    val isRead: Boolean
)
{

    fun toNotification(): NotificationDTO {
        return NotificationDTO(
            id = this.id,
            title = this.title,
            message = this.message,
            mealType = MealType.valueOf(this.mealType.toString()),
            scheduledTime = try {
                LocalDateTime.parse(this.scheduledTime)
            } catch (e: Exception) {
                LocalDateTime.now()
            }.toString(),
            createdAt = try {
                LocalDateTime.parse(this.createdAt)
            } catch (e: Exception) {
                LocalDateTime.now()
            }.toString(),
            isRead = this.isRead,
            isReminderSent = this.isReminderSent
        )
    }
}