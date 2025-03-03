package com.example.biofit.view_model

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.biofit.R
import com.example.biofit.data.dto.LoginRequest
import com.example.biofit.data.dto.UserDTO
import com.example.biofit.data.remote.RetrofitClient
import com.example.biofit.navigation.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var loginState = mutableStateOf<Boolean?>(null) // true: thành công, false: thất bại
    var loginMessage = mutableStateOf<String?>(null) // Lưu thông báo để hiển thị Toast

    fun loginUser(context: Context) {
        val apiService = RetrofitClient.instance

        val loginRequest = LoginRequest(email.value, password.value)
        apiService.login(loginRequest).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    loginState.value = true
                    loginMessage.value =
                        context.getString(R.string.login_successful)  // Xóa lỗi nếu đăng nhập đúng

                    // Chuyển sang MainActivity
                    user?.let {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            putExtra("USER_DATA", it)
                        }
                        context.startActivity(intent)
                    }
                } else {
                    loginState.value = false
                    loginMessage.value = context.getString(R.string.email_or_password_is_incorrect)
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                loginState.value = false
                loginMessage.value = context.getString(R.string.connection_error_please_try_again)
            }
        })
    }
}