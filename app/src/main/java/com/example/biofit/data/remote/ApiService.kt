package com.example.biofit.data.remote

import com.example.biofit.data.model.dto.DailyLogDTO
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
    // Daily Log API
    @GET("api/daily-log/user/{userId}/latest")
    fun getLatestDailyWeight(
        @Path("userId") userId: Long
    ): Call<DailyLogDTO>

    @POST("/api/daily-log/save-or-update")
    fun saveOrUpdateDailyWeight(@Body request: DailyLogDTO): Call<DailyLogDTO>

    @GET("/api/daily-log/user/{userId}/history")
    fun getWeightHistory(@Path("userId") userId: Long): Call<List<DailyLogDTO>>
}