package com.example.biofit.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeightDTO(
    val dailyWeightId: Long? = null,
    val userId: Long,
    val weight: Float,
    val date: String
) : Parcelable {
    companion object {
        fun default(): DailyWeightDTO {
            return DailyWeightDTO(
                dailyWeightId = 0,
                userId = 0,
                weight = 0f,
                date = "N/A"
            )
        }
    }
}