package com.example.biofit.repository

import com.example.biofit.model.LoginRequest
import com.example.biofit.model.User
import com.example.biofit.service.RetrofitInstance
import com.example.biofit.service.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {
    private val api = RetrofitInstance.api

    suspend fun loginUser(email: String, password: String): User? {
        val response = api.loginUser(LoginRequest(email, password))
        return if (response.isSuccessful) response.body() else null
    }
}
