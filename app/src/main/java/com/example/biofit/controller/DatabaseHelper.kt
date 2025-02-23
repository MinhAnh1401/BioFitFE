package com.example.biofit.controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.biofit.model.UserData

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        Log.d("Database", "Đã tạo bảng User") // Kiểm tra xem bảng có được tạo không
        val createTableQuery = """
        CREATE TABLE User (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            hobbies TEXT,
            weight REAL,
            height REAL
        )
    """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
    }
}

class DatabaseHelper(context: Context) {
    private val dbHelper = SQLiteHelper(context)

    fun getUserData(): UserData? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User LIMIT 1", null)
        var userData: UserData? = null
        if (cursor.moveToFirst()) {
            userData = UserData(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                hobbies = cursor.getString(2),
                weight = cursor.getFloat(3),
                height = cursor.getFloat(4)
            )
            Log.d("Database", "Dữ liệu người dùng: $userData") // Xem dữ liệu có được lấy ra không
        } else {
            Log.d("Database", "Không có dữ liệu trong User")
        }
        cursor.close()
        return userData
    }

    fun addUserData(userData: UserData) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", userData.name)
            put("hobbies", userData.hobbies)
            put("weight", userData.weight)
            put("height", userData.height)
        }
        db.insert("User", null, values)
        db.close() // Đóng database sau khi thêm
    }

}