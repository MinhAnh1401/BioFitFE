package com.example.biofit.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyLogDTO(
    val dailyWeightId: Long? = null,
    val userId: Long,
    val weight: Float,
    val water: Float,
    val date: String
) : Parcelable {
    companion object {
        fun default(): DailyLogDTO {
            return DailyLogDTO(
                dailyWeightId = 0,
                userId = 0,
                weight = 0f,
                water = 0f,
                date = "N/A"
            )
        }
    }
}