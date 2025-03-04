package com.example.biofit.data.dto

data class UpdateUserRequest(
    val fullName: String?,
    val email: String?,
    val gender: Int?,
    val dateOfBirth: String?,
    val height: Float?,
    val weight: Float?,
    val targetWeight: Float?,
    val avatar: String?
)