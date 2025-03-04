package com.example.biofit.view_model

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.biofit.R
import com.example.biofit.data.dto.LoginRequest
import com.example.biofit.data.dto.UserDTO
import com.example.biofit.data.remote.RetrofitClient
import com.example.biofit.navigation.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var loginState = mutableStateOf<Boolean?>(null) // true: thành công, false: thất bại
    var loginMessage = mutableStateOf<String?>(null) // Lưu thông báo để hiển thị Toast

    private val _userData = MutableStateFlow<UserDTO?>(null)
    val userData: StateFlow<UserDTO?> = _userData.asStateFlow()

    fun loginUser(context: Context) {
        val apiService = RetrofitClient.instance

        val loginRequest = LoginRequest(email.value, password.value)
        apiService.login(loginRequest).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("LoginViewModel", "User received from API: $user")

                    loginState.value = true
                    loginMessage.value =
                        context.getString(R.string.login_successful)

                    user?.let {
                        _userData.value = it
                        Log.d("LoginViewModel", "userData updated: ${_userData.value}")
                        saveUserData(context, it)
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        getApplication<Application>().startActivity(intent)
                    }
                } else {
                    Log.e("LoginViewModel", "Login failed: ${response.errorBody()?.string()}")
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

    fun saveUserData(context: Context, user: UserDTO) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(user) // Chuyển UserDTO thành JSON
        editor.putString("USER_DATA", json)
        editor.apply() // Lưu lại
    }
}