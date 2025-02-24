package com.example.biofit.controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.biofit.model.UserData

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
        CREATE TABLE User (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            fullName TEXT,
            email TEXT,
            password TEXT,
            gender INTEGER CHECK (gender IN (0, 1)),
            dateOfBirth TEXT,
            height REAL,
            weight REAL,
            targetWeight REAL
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
        private const val DATABASE_VERSION = 3
    }
}

class DatabaseHelper(context: Context) {
    private val dbHelper = SQLiteHelper(context)

    fun getUserDataById(userId: Int): UserData? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User WHERE id = ?", arrayOf(userId.toString()))
        var userData: UserData? = null
        if (cursor.moveToFirst()) {
            userData = UserData(
                id = cursor.getInt(0),
                fullName = cursor.getString(1),
                email = cursor.getString(2),
                password = cursor.getString(3),
                gender = cursor.getInt(4),
                dateOfBirth = cursor.getString(5),
                height = cursor.getFloat(6),
                weight = cursor.getFloat(7),
                targetWeight = cursor.getFloat(8)
            )
            Log.d("Database", "Dữ liệu người dùng: $userData") // Xem dữ liệu có được lấy ra không
        } else {
            Log.d("Database", "Không có dữ liệu trong User")
        }
        cursor.close()
        db.close()
        return userData
    }

    fun addUserData(userData: UserData) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("fullName", userData.fullName)
            put("email", userData.email)
            put("password", userData.password)
            put("gender", userData.gender)
            put("dateOfBirth", userData.dateOfBirth)
            put("height", userData.height)
            put("weight", userData.weight)
            put("targetWeight", userData.targetWeight)
        }
        db.insert("User", null, values)
        db.close() // Đóng database sau khi thêm
    }

}