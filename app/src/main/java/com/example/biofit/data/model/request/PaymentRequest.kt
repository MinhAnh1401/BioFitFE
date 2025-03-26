package com.example.biofit.data.model.request

data class PaymentRequest (
    val userId: Long,
    val planType: String,
    val paymentMethod: String,
    val returnUrl: String?,
    val ipAddress: String?, // Địa chỉ IP thực tế của người dùng
    val amount: Long? = null,  // Số tiền thanh toán
)
