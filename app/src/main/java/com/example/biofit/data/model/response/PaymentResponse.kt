package com.example.biofit.data.model.response

data class PaymentResponse(
    val success: Boolean,
    val message: String,
    val paymentUrl: String?,
    val orderId: String?
)