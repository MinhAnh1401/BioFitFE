// LoginController.kt
package com.example.biofit.controller

import android.content.Context
import android.widget.Toast
import com.example.biofit.model.UserData
import com.example.biofit.utils.SecurityUtils

class LoginController(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    /**
     * Xác thực thông tin đăng nhập
     * @return UserData nếu đăng nhập thành công, null nếu thất bại
     */
    fun loginUser(email: String, password: String): UserData? {
        // Kiểm tra email và mật khẩu có trống không
        if (email.isBlank()) {
            Toast.makeText(context, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
            return null
        }

        if (password.isBlank()) {
            Toast.makeText(context, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show()
            return null
        }

        // Lấy thông tin người dùng từ database
        val user = dbHelper.getUserByEmail(email)

        // Kiểm tra người dùng và mật khẩu
        return when {
            user == null -> {
                Toast.makeText(context, "Email không tồn tại", Toast.LENGTH_SHORT).show()
                null
            }
            !SecurityUtils.verifyPassword(password, user.password) -> {
                Toast.makeText(context, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show()
                null
            }
            else -> user // Đăng nhập thành công
        }
    }
}