package com.example.biofit.service

import com.example.biofit.model.LoginRequest
import com.example.biofit.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("api/auth/login") // Thay đổi endpoint theo server của bạn
    suspend fun loginUser(@Body request: LoginRequest): Response<User>
}