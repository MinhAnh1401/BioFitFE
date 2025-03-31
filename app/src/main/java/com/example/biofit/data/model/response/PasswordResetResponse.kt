package com.example.biofit.data.model.response

data class PasswordResetResponse(
    val message: String,
    val success: Boolean,
    val resetCode: String? = null // resetCode, có thể null nếu thất bại
)