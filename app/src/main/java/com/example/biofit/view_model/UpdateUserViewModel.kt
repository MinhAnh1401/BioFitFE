package com.example.biofit.view_model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.biofit.R
import com.example.biofit.data.model.UpdateUserRequest
import com.example.biofit.data.model.UserDTO
import com.example.biofit.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserViewModel : ViewModel() {
    var fullName = mutableStateOf<String?>(null)
    var gender = mutableStateOf<Int?>(null)
    var dateOfBirth = mutableStateOf<String?>(null)
    var height = mutableStateOf<Float?>(null)
    var weight = mutableStateOf<Float?>(null)
    var targetWeight = mutableStateOf<Float?>(null)
    var avatar = mutableStateOf<String?>(null)
    var updatedState = mutableStateOf<Boolean?>(null)
    var updatedMessage = mutableStateOf<String?>(null)

    fun updateUser(context: Context, userId: Long, onSuccess: () -> Unit) {
        val apiService = RetrofitClient.instance

        val updateUserRequest = UpdateUserRequest(
            fullName.value,
            gender.value,
            dateOfBirth.value,
            height.value,
            weight.value,
            targetWeight.value,
            avatar.value
        )

        apiService.updateUser(userId, updateUserRequest).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    updatedState.value = true

                    user?.let { updatedUser ->
                        fullName.value = updatedUser.fullName
                        gender.value = updatedUser.gender
                        dateOfBirth.value = updatedUser.dateOfBirth
                        height.value = updatedUser.height
                        weight.value = updatedUser.weight
                        targetWeight.value = updatedUser.targetWeight
                        avatar.value = updatedUser.avatar
                    }
                    onSuccess()
                } else {
                    updatedState.value = false
                    updatedMessage.value = "Failed to update user"
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                updatedState.value = false
                updatedMessage.value = context.getString(R.string.connection_error_please_try_again)
            }
        })
    }
}