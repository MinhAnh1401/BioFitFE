package com.example.biofit.data.model.dto

data class PasswordResetDTO(
    val email: String,
    val resetCode: String,
    val newPassword: String
)