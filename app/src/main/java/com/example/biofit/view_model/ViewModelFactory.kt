package com.example.biofit.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.biofit.data.model.repository.UserRepository
import com.example.biofit.data.remote.RetrofitClient

// cái này riêng của manh
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordResetViewModel::class.java)) {
            val userRepository = UserRepository(RetrofitClient.instance)
            return PasswordResetViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}