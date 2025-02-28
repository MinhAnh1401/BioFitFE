package com.example.biofit.model

import android.content.Context
import com.example.biofit.R

data class User(
    val userId: Long?,
    val fullName: String?,
    val email: String,
    val hashPassword: String,
    val gender: Int?,
    val dateOfBirth: String?,
    val avatar: String?,
    val createdAccount: String,
) {
    fun getGenderText(context: Context): String {
        return when (gender) {
            0 -> context.getString(R.string.male)
            else -> context.getString(R.string.female)
        }
    }
}