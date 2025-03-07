package com.example.biofit.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.biofit.R
import com.example.biofit.data.model.dto.DailyWeightDTO
import com.example.biofit.data.remote.RetrofitClient
import com.example.biofit.data.utils.DailyWeightSharedPrefsHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyWeightViewModel : ViewModel() {
    var userId: Long = 0L
    var weight = mutableStateOf<Float?>(null)
    var date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    var saveState = mutableStateOf<Boolean?>(null)
    var saveMessage = mutableStateOf<String?>(null)

    fun saveDailyWeight(context: Context) {
        val apiService = RetrofitClient.instance

        val request = DailyWeightDTO(
            dailyWeightId = null,
            userId = userId,
            weight = weight.value ?: memoryWeight.value,
            date = date
        )

        apiService.saveOrUpdateDailyWeight(request).enqueue(object : Callback<DailyWeightDTO> {
            override fun onResponse(call: Call<DailyWeightDTO>, response: Response<DailyWeightDTO>) {
                if (response.isSuccessful) {
                    val latestWeight = response.body()
                    if (latestWeight != null) {
                        saveState.value = true
                        saveMessage.value = context.getString(R.string.update_daily_weight_successfully)

                        DailyWeightSharedPrefsHelper.saveDailyWeight(context, latestWeight)
                    } else {
                        saveState.value = false
                        saveMessage.value = context.getString(R.string.update_failed)
                    }
                } else {
                    saveState.value = false
                    saveMessage.value = context.getString(R.string.update_failed)
                }
            }

            override fun onFailure(call: Call<DailyWeightDTO>, t: Throwable) {
                saveState.value = false
                saveMessage.value = context.getString(R.string.connection_error_please_try_again)
            }
        })
    }

    var memoryWeight = mutableStateOf(0f)

    var getState = mutableStateOf<Boolean?>(null)

    fun getLatestDailyWeight(context: Context) {
        if (userId == 0L) {
            return
        }

        val cachedWeight = DailyWeightSharedPrefsHelper.getDailyWeight(context)
        if (cachedWeight != null && cachedWeight.userId == userId) {
            memoryWeight.value = cachedWeight.weight
        }

        val apiService = RetrofitClient.instance

        apiService.getLatestDailyWeight(userId).enqueue(object : Callback<DailyWeightDTO> {
            override fun onResponse(call: Call<DailyWeightDTO>, response: Response<DailyWeightDTO>) {
                if (response.isSuccessful) {
                    getState.value = true
                    val dailyWeight = response.body()

                    memoryWeight.value = dailyWeight?.weight ?: 0f

                    dailyWeight?.let {
                        DailyWeightSharedPrefsHelper.saveDailyWeight(context, it)
                        memoryWeight.value = it.weight // 🔥 Cập nhật UI sau khi đã lưu xong
                    }
                } else {
                    getState.value = false
                }
            }

            override fun onFailure(call: Call<DailyWeightDTO>, t: Throwable) {
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
        apiService.getWeightHistory(userId).enqueue(object : Callback<List<DailyWeightDTO>> {
            override fun onResponse(call: Call<List<DailyWeightDTO>>, response: Response<List<DailyWeightDTO>>) {

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


            override fun onFailure(call: Call<List<DailyWeightDTO>>, t: Throwable) {
                getWeightHistoryState.value = false
            }
        })
    }
}