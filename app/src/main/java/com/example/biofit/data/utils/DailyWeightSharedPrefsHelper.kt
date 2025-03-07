package com.example.biofit.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.biofit.data.model.dto.DailyWeightDTO
import com.google.gson.Gson

object DailyWeightSharedPrefsHelper {
    private const val PREF_NAME = "UserPrefs"
    private const val DAILY_WEIGHT_KEY = "DAILY_WEIGHT"

    fun saveDailyWeight(context: Context, dailyWeight: DailyWeightDTO) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(dailyWeight)

        editor.putString(DAILY_WEIGHT_KEY, json)
        editor.apply()
    }

    fun getDailyWeight(context: Context): DailyWeightDTO? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(DAILY_WEIGHT_KEY, null)

        return gson.fromJson(json, DailyWeightDTO::class.java)
    }
}
