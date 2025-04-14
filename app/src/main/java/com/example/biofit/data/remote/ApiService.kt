package com.example.biofit.data.remote

import com.example.biofit.data.model.dto.DailyLogDTO
import com.example.biofit.data.model.dto.ExerciseDTO
import com.example.biofit.data.model.dto.ExerciseDoneDTO
import com.example.biofit.data.model.dto.FoodDTO
import com.example.biofit.data.model.dto.FoodDoneDTO
import com.example.biofit.data.model.dto.NotificationDTO
import com.example.biofit.data.model.dto.FoodSummaryDTO
import com.example.biofit.data.model.dto.OverviewExerciseDTO
import com.example.biofit.data.model.dto.PasswordResetDTO
import com.example.biofit.data.model.dto.UserDTO
import com.example.biofit.data.model.request.LoginRequest
import com.example.biofit.data.model.request.PasswordResetRequest
import com.example.biofit.data.model.request.PaymentRequest
import com.example.biofit.data.model.request.RegisterRequest
import com.example.biofit.data.model.response.PasswordResetResponse
import com.example.biofit.data.model.response.PaymentResponse
import com.example.biofit.data.model.response.SubscriptionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // User API
    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<UserDTO>

    @POST("api/user/register")
    fun register(@Body request: RegisterRequest): Call<UserDTO>

    @PUT("api/user/update/{userId}")
    @Multipart
    fun updateUser(
        @Path("userId") userId: Long,
        @Part avatar: MultipartBody.Part?,
        @Part("user") user: RequestBody
    ): Call<UserDTO>


    /*
    ------------------------------------------------------------------------------------------------
    */
    // Daily Log API
    @GET("api/daily-log/user/{userId}/latest")
    fun getLatestDailyLog(
        @Path("userId") userId: Long
    ): Call<DailyLogDTO>

    @POST("/api/daily-log/save-or-update")
    fun saveOrUpdateDailyWeight(@Body request: DailyLogDTO): Call<DailyLogDTO>

    @GET("/api/daily-log/user/{userId}/history")
    fun getWeightHistory(@Path("userId") userId: Long): Call<List<DailyLogDTO>>

    /*
    ------------------------------------------------------------------------------------------------
    */
    // Exercise API
    @GET("api/exercise/user/{userId}")
    fun getExercises(@Path("userId") userId: Long): Call<List<ExerciseDTO>>

    @GET("api/exercise/{exerciseId}/details")
    fun getExerciseByGoalAndIntensity(
        @Path("exerciseId") exerciseId: Long,
        @Query("exerciseGoal") exerciseGoal: Int,
        @Query("intensity") intensity: Int
    ): Call<ExerciseDTO>

    @POST("api/exercise/create")
    fun createExercise(@Body exerciseDTO: ExerciseDTO): Call<ExerciseDTO>

    @DELETE("api/exercise/{exerciseId}")
    fun deleteExercise(@Path("exerciseId") exerciseId: Long): Call<Void>

    @PUT("api/exercise/{exerciseId}")
    fun updateExercise(
        @Path("exerciseId") exerciseId: Long,
        @Body exercise: ExerciseDTO
    ): Call<Void>
    /*
    ------------------------------------------------------------------------------------------------
    */
    // Exercise Done API
    @POST("api/exercise-done/create")
    fun createExerciseDone(@Body exerciseDoneDTO: ExerciseDoneDTO): Call<ExerciseDoneDTO>

    @GET("api/exercise-done/overview")
    fun getOverviewExercises(
        @Query("userId") userId: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Call<List<OverviewExerciseDTO>>

    @GET("api/exercise-done/burned-calories/today")
    fun getBurnedCaloriesToday(
        @Query("userId") userId: Long
    ): Call<Float>

    @GET("api/exercise-done/time/today")
    fun getExerciseDoneTimeToday(
        @Query("userId") userId: Long
    ): Call<Float>

    /*
    --------------------------------------------------------------------------------------------
    */

    // Google API
    //    @POST("api/auth/google")
    //    suspend fun googleSignIn(@Body request: GoogleAuthDTO): Response<SocialAccountDTO>

    @POST("api/payment/create-payment")
    fun createPayment(@Body request: PaymentRequest): Call<PaymentResponse>

    @GET("subscription/status/{userId}")
    fun getSubscriptionStatus(@Path("userId") userId: Long): Call<SubscriptionResponse>

    @GET("subscription/status_sub/{userId}")
    suspend fun checkSubscription(@Path("userId") userId: Long): Boolean

    @GET("/api/subscription/latest/{userId}")
    fun getLatestSubscription(@Path("userId") userId: Long): Call<SubscriptionResponse>

    /*
    ------------------------------------------------------------------------------------------------
    */

    // reset password API
    @POST("api/user/forgot-password")
    suspend fun requestPasswordReset(@Body request: PasswordResetRequest): Response<PasswordResetResponse>

    @POST("api/user/reset-password")
    suspend fun resetPassword(@Body request: PasswordResetDTO): Response<PasswordResetResponse>

    /*
    ------------------------------------------------------------------------------------------------
    */
    // API Food

    @GET("api/food/user/{userId}")
    fun getFood(@Path("userId") userId: Long): Call<List<FoodDTO>>

    @POST("api/food/create")
    @Multipart
    fun createFood(
        @Part("food") foodJson: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<FoodDTO>

    @DELETE("api/food/{foodId}")
    fun deleteFood(@Path("foodId") foodId: Long): Call<Void>

    @PUT("api/food/{foodId}")
    fun updateFood(
        @Path("foodId") foodId: Long,
        @Body foodDTO: FoodDTO
    ): Call<FoodDTO>

    @Multipart
    @PUT("api/food/{foodId}")
    fun updateFoodWithImage(
        @Path("foodId") foodId: Long,
        @Part("food") foodJson: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<FoodDTO>



    /*
       ----------------------------------------------------------------------------------------------------
       */
    // Food Done API
    @POST("api/food-done/create")
    fun createFoodDone(@Body foodDoneDTO: FoodDoneDTO): Call<FoodDoneDTO>

    @GET("api/food-done/list")
    fun getFoodDoneByDate(
        @Query("userId") userId: Long,
        @Query("date") date: String
    ): Call<List<FoodDoneDTO>>

    @GET("api/food-done/summary")
    fun getFoodSummary(
        @Query("userId") userId: Long,
        @Query("date") date: String
    ): Call<FoodSummaryDTO>

    // xóa thức ăn đã ăn
    @DELETE("api/food-done/delete/{id}")
    fun deleteFoodDone(@Path("id") foodDoneId: Long): Call<Void>

    /*
    ------------------------------------------------------------------------------------------------
    */
    // API Notification

    @GET("api/notification/{userId}")
    suspend fun getUserNotifications(@Path("userId") userId: String): Response<List<NotificationDTO>>

    @DELETE("api/notification/{id}")
    suspend fun deleteNotification(@Path("id") id: Long): Response<Void>

    @PUT("api/notification/{id}/read")
    suspend fun markAsRead(@Path("id") id: Long): Response<Void>

    @POST("api/notification/mark-all-read/{userId}")
    suspend fun markAllAsRead(
        @Path("userId") userId: String,
        @Body notificationIds: List<Long>
    ): Response<Void>

    @POST("api/notification/welcome/{userId}")
    suspend fun sendWelcomeNotification(@Path("userId") userId: String): Response<NotificationDTO>

    @DELETE("notifications/delete-all/{userId}")
    suspend fun deleteAllNotifications(@Path("userId") userId: String)

}
