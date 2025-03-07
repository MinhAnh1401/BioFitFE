package com.example.biofit.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.biofit.R
import com.example.biofit.data.model.dto.DailyLogDTO
import com.example.biofit.data.remote.RetrofitClient
import com.example.biofit.data.utils.DailyLogSharedPrefsHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyLogViewModel : ViewModel() {
    var userId: Long = 0L
    var weight = mutableStateOf<Float?>(null)
    var water = mutableStateOf<Float?>(null)
    var date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    var saveState = mutableStateOf<Boolean?>(null)
    var saveMessage = mutableStateOf<String?>(null)

    fun saveDailyLog(context: Context) {
        val apiService = RetrofitClient.instance

        val request = DailyLogDTO(
            dailyWeightId = null,
            userId = userId,
            weight = weight.value ?: memoryWeight.value,
            water = water.value ?: memoryWater.value,
            date = date
        )

        if (DailyLogSharedPrefsHelper.getDailyLog(context)?.date != date) {
            val oldLog = DailyLogSharedPrefsHelper.getDailyLog(context)

            val newWeight = if (weight.value != oldLog?.weight) weight.value else null
            val newWater = if (water.value != oldLog?.water) water.value else null

            if (newWeight == null && newWater == null) {
                saveState.value = false
                return
            }
            if (newWeight != null && newWater == null) {
                val requestWe = DailyLogDTO(
                    dailyWeightId = null,
                    userId = userId,
                    weight = newWeight,
                    water = 0f,
                    date = date
                )

                apiService.saveOrUpdateDailyWeight(requestWe).enqueue(object : Callback<DailyLogDTO> {
                    override fun onResponse(call: Call<DailyLogDTO>, response: Response<DailyLogDTO>) {
                        if (response.isSuccessful) {
                            val latestLog = response.body()
                            if (latestLog != null) {
                                saveState.value = true
                                saveMessage.value = context.getString(R.string.update_daily_weight_successfully)

                                DailyLogSharedPrefsHelper.saveDailyLog(context, latestLog)
                            } else {
                                saveState.value = false
                                saveMessage.value = context.getString(R.string.update_failed)
                            }
                        } else {
                            saveState.value = false
                            saveMessage.value = context.getString(R.string.update_failed)
                        }
                    }

                    override fun onFailure(call: Call<DailyLogDTO>, t: Throwable) {
                        saveState.value = false
                        saveMessage.value = context.getString(R.string.connection_error_please_try_again)
                    }
                })
            }
            if (newWeight == null && newWater != null) {
                val requestWa = DailyLogDTO(
                    dailyWeightId = null,
                    userId = userId,
                    weight = memoryWeight.value,
                    water = newWater,
                    date = date
                )

                apiService.saveOrUpdateDailyWeight(requestWa).enqueue(object : Callback<DailyLogDTO> {
                    override fun onResponse(call: Call<DailyLogDTO>, response: Response<DailyLogDTO>) {
                        if (response.isSuccessful) {
                            val latestLog = response.body()
                            if (latestLog != null) {
                                saveState.value = true
                                saveMessage.value = context.getString(R.string.update_daily_weight_successfully)

                                DailyLogSharedPrefsHelper.saveDailyLog(context, latestLog)
                            } else {
                                saveState.value = false
                                saveMessage.value = context.getString(R.string.update_failed)
                            }
                        } else {
                            saveState.value = false
                            saveMessage.value = context.getString(R.string.update_failed)
                        }
                    }

                    override fun onFailure(call: Call<DailyLogDTO>, t: Throwable) {
                        saveState.value = false
                        saveMessage.value = context.getString(R.string.connection_error_please_try_again)
                    }
                })
            }
        } else {
            apiService.saveOrUpdateDailyWeight(request).enqueue(object : Callback<DailyLogDTO> {
                override fun onResponse(call: Call<DailyLogDTO>, response: Response<DailyLogDTO>) {
                    if (response.isSuccessful) {
                        val latestLog = response.body()
                        if (latestLog != null) {
                            saveState.value = true
                            saveMessage.value =
                                context.getString(R.string.update_daily_weight_successfully)

                            DailyLogSharedPrefsHelper.saveDailyLog(context, latestLog)
                        } else {
                            saveState.value = false
                            saveMessage.value = context.getString(R.string.update_failed)
                        }
                    } else {
                        saveState.value = false
                        saveMessage.value = context.getString(R.string.update_failed)
                    }
                }

                override fun onFailure(call: Call<DailyLogDTO>, t: Throwable) {
                    saveState.value = false
                    saveMessage.value =
                        context.getString(R.string.connection_error_please_try_again)
                }
            })
        }
    }

    var memoryWeight = mutableStateOf(0f)
    var memoryWater = mutableStateOf(0f)

    var getState = mutableStateOf<Boolean?>(null)

    fun getLatestDailyLog(context: Context) {
        if (userId == 0L) {
            return
        }

        val cachedLog = DailyLogSharedPrefsHelper.getDailyLog(context)
        if (cachedLog != null && cachedLog.userId == userId) {
            memoryWeight.value = cachedLog.weight
            memoryWater.value = cachedLog.water
        }

        val apiService = RetrofitClient.instance

        apiService.getLatestDailyWeight(userId).enqueue(object : Callback<DailyLogDTO> {
            override fun onResponse(call: Call<DailyLogDTO>, response: Response<DailyLogDTO>) {
                if (response.isSuccessful) {
                    getState.value = true
                    val dailyLog = response.body()

                    memoryWeight.value = dailyLog?.weight ?: 0f
                    memoryWater.value = dailyLog?.water ?: 0f

                    dailyLog?.let {
                        DailyLogSharedPrefsHelper.saveDailyLog(context, it)
                        memoryWeight.value = it.weight // 🔥 Cập nhật UI sau khi đã lưu xong
                        memoryWater.value = it.water
                    }
                } else {
                    getState.value = false
                }
            }

            override fun onFailure(call: Call<DailyLogDTO>, t: Throwable) {
                getState.value = false
            }
        })
    }

    fun updateUserId(userId: Long) {
        this.userId = userId
    }

    var weightDataState = mutableStateOf<List<Pair<String, Float>>>(emptyList())

    var getWeightHistoryState = mutableStateOf<Boolean?>(null)

    fun getWeightHistory(userId: Long) {
        val apiService = RetrofitClient.instance

        val mainHandler = Handler(Looper.getMainLooper())
        apiService.getWeightHistory(userId).enqueue(object : Callback<List<DailyLogDTO>> {
            override fun onResponse(call: Call<List<DailyLogDTO>>, response: Response<List<DailyLogDTO>>) {

                if (response.isSuccessful) {
                    val weightHistory = response.body()
                    if (weightHistory != null) {

                        val formatterDay = DateTimeFormatter.ofPattern("dd")
                        val formatterMonth = DateTimeFormatter.ofPattern("MM")

                        val weightData = weightHistory.map { weight ->
                            val localDate = LocalDate.parse(weight.date)
                            "${localDate.format(formatterDay)}/${localDate.format(formatterMonth)}" to weight.weight
                        }

                        mainHandler.post {
                            weightDataState.value = weightData
                        }

                        getWeightHistoryState.value = true
                    } else {
                        getWeightHistoryState.value = false
                    }
                } else {
                    getWeightHistoryState.value = false
                }
            }


            override fun onFailure(call: Call<List<DailyLogDTO>>, t: Throwable) {
                getWeightHistoryState.value = false
            }
        })
    }
}