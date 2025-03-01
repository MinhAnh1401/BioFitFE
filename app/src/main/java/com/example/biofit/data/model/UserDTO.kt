package com.example.biofit.data.model

import android.content.Context
import android.os.Parcelable
import com.example.biofit.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDTO(
    val userId: Long,
    val fullName: String?,
    val email: String,
    val gender: Int?,
    val height: Float?,
    val weight: Float?,
    val targetWeight: Float?,
    val dateOfBirth: String?,
    val avatar: String?,
    val createdAccount: String
) : Parcelable {
    fun getGenderString(context: Context): String {
        return when (gender) {
            0 -> context.getString(R.string.male)
            else -> context.getString(R.string.female)
        }
    }
}