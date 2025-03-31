package com.example.biofit.data.model.repository

import com.example.biofit.data.model.dto.PasswordResetDTO
import com.example.biofit.data.model.request.PasswordResetRequest
import com.example.biofit.data.model.response.PasswordResetResponse
import com.example.biofit.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class UserRepository(private val apiService: ApiService) {

    suspend fun requestPasswordReset(email: String): Result<PasswordResetResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = PasswordResetRequest(email)
                val response = apiService.requestPasswordReset(request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(IOException("Empty response body"))
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(IOException(errorBody))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun resetPassword(
        email: String,
        resetCode: String,
        newPassword: String
    ): Result<PasswordResetResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = PasswordResetDTO(email, resetCode, newPassword)
                val response = apiService.resetPassword(request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(IOException("Empty response body"))
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(IOException(errorBody))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

}