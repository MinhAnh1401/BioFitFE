package com.example.biofit.controller

import android.content.Context
import com.example.biofit.model.UserData

class RegistrationController(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    /**
     * Hàm này xử lý đăng ký người dùng.
     * @param email Email của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return Boolean Trả về true nếu đăng ký thành công, ngược lại trả về false.
     */
    fun registerUser(email: String, password: String): Boolean {
        // Kiểm tra xem email và password có hợp lệ không
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }

        // Mã hóa mật khẩu
        val hashedPassword = PasswordUtils.hashPassword(password)

        // Tạo đối tượng UserData (các trường khác có thể để mặc định hoặc thêm sau)
        val userData = UserData(
            id = 0, // ID sẽ được tự động tạo bởi cơ sở dữ liệu
            fullName = "", // Có thể thêm sau
            email = email,
            password = hashedPassword,
            gender = 0, // Mặc định
            dateOfBirth = "", // Có thể thêm sau
            height = 0f, // Mặc định
            weight = 0f, // Mặc định
            targetWeight = 0f // Mặc định
        )

        // Lưu dữ liệu vào cơ sở dữ liệu
        dbHelper.addUserData(userData)

        return true
    }
}