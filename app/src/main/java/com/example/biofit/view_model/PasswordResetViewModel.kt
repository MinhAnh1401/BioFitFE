package com.example.biofit.view_model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biofit.data.model.repository.UserRepository
import kotlinx.coroutines.launch

class PasswordResetViewModel(private val repository: UserRepository) : ViewModel() {

    // Trạng thái cho quy trình yêu cầu đặt lại mật khẩu
    private val _requestResetState = MutableLiveData<RequestResetState>()
    val requestResetState: LiveData<RequestResetState> = _requestResetState

    // Trạng thái cho quy trình xác nhận đặt lại mật khẩu
    private val _confirmResetState = MutableLiveData<ConfirmResetState>()
    val confirmResetState: LiveData<ConfirmResetState> = _confirmResetState

    // Email đang được sử dụng để thiết lập lại
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _resetCode = MutableLiveData<String?>()
    val resetCode: LiveData<String?> = _resetCode

    // Cập nhật email
    fun setEmail(email: String) {
        _email.value = email
    }

    // Yêu cầu mã đặt lại mật khẩu cho email đã cho
    fun requestPasswordReset(email: String) {
        _requestResetState.value = RequestResetState.Loading

        viewModelScope.launch {
            repository.requestPasswordReset(email).fold(
                onSuccess = { response ->
                    if (response.success) {
                        _requestResetState.value = RequestResetState.Success(
                            message = response.message,
                            resetCode = response.resetCode // Truyền resetCode
                        )
                        _resetCode.value = response.resetCode // Lưu resetCode
                        setEmail(email)
                    } else {
                        _requestResetState.value = RequestResetState.Error(response.message)
                    }
                },
                onFailure = { exception ->
                    _requestResetState.value = RequestResetState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    // Xác nhận đặt lại mật khẩu bằng mã và mật khẩu mới
    fun confirmPasswordReset(email: String, resetCode: String, newPassword: String) {
        _confirmResetState.value = ConfirmResetState.Loading

        viewModelScope.launch {
            repository.resetPassword(email, resetCode, newPassword).fold(
                onSuccess = { response ->
                    Log.d("PasswordResetViewModel", "Reset Password Response: $response")
                    if (response.success) {
                        _confirmResetState.value = ConfirmResetState.Success(response.message)
                    } else {
                        _confirmResetState.value = ConfirmResetState.Error(response.message)
                    }
                },
                onFailure = { exception ->
                    _confirmResetState.value = ConfirmResetState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    // Đặt lại trạng thái để tránh hiển thị các tin nhắn cũ
//    fun resetRequestState() {
//        _requestResetState.value = RequestResetState.Initial
//    }

    fun resetConfirmState() {
        _confirmResetState.value = ConfirmResetState.Initial
    }

    // State classes
    sealed class RequestResetState {
        object Initial : RequestResetState()
        object Loading : RequestResetState()
        data class Success(val message: String, val resetCode: String?) : RequestResetState()
        data class Error(val message: String) : RequestResetState()
    }

    sealed class ConfirmResetState {
        object Initial : ConfirmResetState()
        object Loading : ConfirmResetState()
        data class Success(val message: String) : ConfirmResetState()
        data class Error(val message: String) : ConfirmResetState()
    }
}