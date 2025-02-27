
package com.example.biofit.utils
import at.favre.lib.crypto.bcrypt.BCrypt

object SecurityUtils { // Đổi tên object thành SecurityUtils
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
    /**
     * Kiểm tra định dạng email có hợp lệ không (phải kết thúc bằng @gmail.com)
     */
    fun isValidGmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$")
        return regex.matches(email)
    }
}