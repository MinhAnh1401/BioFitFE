package com.example.biofit.model

import android.content.Context
import com.example.biofit.R

data class UserData(
    val id: Int,
    val fullName: String,
    val email: String,
    val password: String,
    val gender: Int,
    val dateOfBirth: String,
    val height: Float,
    val weight: Float,
    val targetWeight: Float
) {
    fun getGenderText(context: Context): String {
        return when (gender) {
            0 -> context.getString(R.string.male)
            else -> context.getString(R.string.female)
        }
    }
}