package com.example.biofit.data.remote

import com.example.biofit.data.model.dto.DailyWeightDTO
import com.example.biofit.data.model.request.LoginRequest
import com.example.biofit.data.model.request.RegisterRequest
import com.example.biofit.data.model.request.UpdateUserRequest
import com.example.biofit.data.model.dto.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    // User API
    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<UserDTO>

    @POST("api/user/register")
    fun register(@Body request: RegisterRequest): Call<UserDTO>

    @PUT("api/user/update/{userId}")
    fun updateUser(
        @Path("userId") userId: Long,
        @Body request: UpdateUserRequest
    ): Call<UserDTO>

/*
    ------------------------------------------------------------------------------------------------
*/
    // Daily Weight API
    @GET("api/daily-weight/user/{userId}/latest")
    fun getLatestDailyWeight(
        @Path("userId") userId: Long
    ): Call<DailyWeightDTO>

    @POST("/api/daily-weight/save-or-update")
    fun saveOrUpdateDailyWeight(@Body request: DailyWeightDTO): Call<DailyWeightDTO>

    @GET("/api/daily-weight/user/{userId}/history")
    fun getWeightHistory(@Path("userId") userId: Long): Call<List<DailyWeightDTO>>
}