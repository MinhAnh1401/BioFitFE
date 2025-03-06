package com.example.biofit.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.biofit.data.model.dto.UserDTO
import com.google.gson.Gson

object UserSharedPrefsHelper {
    private const val PREF_NAME = "UserPrefs"
    private const val USER_KEY = "USER_DATA"

    fun getUserData(context: Context): UserDTO? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(USER_KEY, null)

        return gson.fromJson(json, UserDTO::class.java)
    }

}