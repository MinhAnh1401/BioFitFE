// RegistrationController.kt
package com.example.biofit.controller

import android.content.Context
import android.widget.Toast
import com.example.biofit.model.UserData
import com.example.biofit.utils.SecurityUtils

class RegistrationController(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    /**
     * Hàm này xử lý đăng ký người dùng.
     * @param email Email của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return Boolean Trả về true nếu đăng ký thành công, ngược lại trả về false.
     */
    fun registerUser(email: String, password: String): Boolean {
        // Kiểm tra email và password có trống không
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return false
        }

        // Kiểm tra định dạng email
        if (!SecurityUtils.isValidGmail(email)) {
            Toast.makeText(context, "Email phải có định dạng @gmail.com", Toast.LENGTH_SHORT).show()
            return false
        }

        // Mã hóa mật khẩu
        val hashedPassword = SecurityUtils.hashPassword(password)

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

        // Thông báo đăng ký thành công
        Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
        return true
    }
}