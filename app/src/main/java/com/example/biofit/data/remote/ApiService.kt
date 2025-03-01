package com.example.biofit.data.remote

import com.example.biofit.data.model.LoginRequest
import com.example.biofit.data.model.RegisterRequest
import com.example.biofit.data.model.UpdateUserRequest
import com.example.biofit.data.model.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<UserDTO>

    @POST("api/user/register")
    fun register(@Body request: RegisterRequest): Call<UserDTO>

    @PUT("api/user/update/{userId}")
    fun updateUser(
        @Path("userId") userId: Long,
        @Body request: UpdateUserRequest
    ): Call<UserDTO>

}