package com.example.biofit.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDoneDTO(
    val exerciseDoneId: Long,
    val exerciseDetailId: Long,
    val date: String,
    val session: Int
) : Parcelable {
    companion object {
        fun default(): ExerciseDoneDTO {
            return ExerciseDoneDTO(
                exerciseDoneId = 0,
                exerciseDetailId = 0,
                date = "",
                session = 0
            )
        }
    }
}