package com.example.biofit.view_model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.biofit.R
import com.example.biofit.data.dto.UpdateUserRequest
import com.example.biofit.data.dto.UserDTO
import com.example.biofit.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserViewModel : ViewModel() {
    var fullName = mutableStateOf<String?>(null)
    var gender = mutableStateOf<Int?>(null)
    var email = mutableStateOf<String?>(null)
    var dateOfBirth = mutableStateOf<String?>(null)
    var height = mutableStateOf<Float?>(null)
    var weight = mutableStateOf<Float?>(null)
    var targetWeight = mutableStateOf<Float?>(null)
    var avatar = mutableStateOf<String?>(null)
    var updatedState = mutableStateOf<Boolean?>(null)
    var updatedMessage = mutableStateOf<String?>(null)

    fun updateUser(
        context: Context,
        userId: Long,
        loginViewModel: LoginViewModel,
        onSuccess: () -> Unit
    ) {
        /*val emailValidationMessage = validateEmail(context, email.value ?: UserDTO.default().email)
        if (emailValidationMessage != null) {
            updatedState.value = false
            updatedMessage.value = emailValidationMessage
            return
        }*/

        val emailValue = email.value
        if (emailValue != null) { // Chỉ validate khi có email
            val emailValidationMessage = validateEmail(context, emailValue)
            if (emailValidationMessage != null) {
                updatedState.value = false
                updatedMessage.value = emailValidationMessage
                return
            }
        }

        val apiService = RetrofitClient.instance

        val updateUserRequest = UpdateUserRequest(
            fullName.value,
            email.value,
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
                        email.value = updatedUser.email
                        gender.value = updatedUser.gender
                        dateOfBirth.value = updatedUser.dateOfBirth
                        height.value = updatedUser.height
                        weight.value = updatedUser.weight
                        targetWeight.value = updatedUser.targetWeight
                        avatar.value = updatedUser.avatar

                        loginViewModel.saveUserData(context, updatedUser)

                        onSuccess()
                    }
                } else if (response.code() == 403) {
                    updatedState.value = false
                    updatedMessage.value = context.getString(R.string.email_already_exists)
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

    private fun validateEmail(context: Context, email: String): String? {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return if (!email.matches(emailPattern.toRegex())) {
            context.getString(R.string.please_enter_a_valid_email)
        } else {
            null
        }
    }
}
