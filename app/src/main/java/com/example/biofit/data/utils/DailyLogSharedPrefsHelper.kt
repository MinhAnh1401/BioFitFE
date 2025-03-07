package com.example.biofit.data.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.biofit.data.model.dto.DailyLogDTO
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DailyLogSharedPrefsHelper {
    private const val PREF_NAME = "UserPrefs"
    private const val DAILY_LOG_KEY = "DAILY_LOG"

    fun saveDailyLog(context: Context, dailyLog: DailyLogDTO) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(dailyLog)

        editor.putString(DAILY_LOG_KEY, json)
        editor.apply()
    }

    fun getDailyLog(context: Context): DailyLogDTO? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(DAILY_LOG_KEY, null)

        return gson.fromJson(json, DailyLogDTO::class.java)
    }
}
