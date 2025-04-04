package com.example.biofit.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biofit.data.model.dto.ExerciseDTO
import com.example.biofit.data.model.dto.ExerciseDetailDTO
import com.example.biofit.data.model.dto.ExerciseDoneDTO
import com.example.biofit.data.model.dto.OverviewExerciseDTO
import com.example.biofit.data.remote.RetrofitClient
import com.example.biofit.data.utils.OverviewExerciseSharedPrefsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseViewModel : ViewModel() {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    private val _exerciseList = MutableStateFlow<List<ExerciseDTO>>(emptyList())
    val exerciseList: StateFlow<List<ExerciseDTO>> = _exerciseList.asStateFlow()

    init {
        viewModelScope.launch {
            userId.collect { id ->
                id?.let { fetchExercises(it) }
            }
        }
    }

    fun setUserId(newUserId: Long) {
        _userId.value = newUserId
    }

    fun fetchExercises(userId: Long) {
        val apiService = RetrofitClient.instance

        apiService.getExercises(userId).enqueue(object : Callback<List<ExerciseDTO>> {
            override fun onResponse(
                call: Call<List<ExerciseDTO>>,
                response: Response<List<ExerciseDTO>>
            ) {
                if (response.isSuccessful) {
                    _exerciseList.value = response.body() ?: emptyList()
                    Log.d("ExerciseViewModel", "API Response: ${response.body()}")
                    Log.d("ExerciseViewModel", "Received exercises: ${_exerciseList.value}")
                } else {
                    Log.e("ExerciseViewModel", "API Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ExerciseDTO>>, t: Throwable) {
                Log.e("ExerciseViewModel", "Network Error", t)
            }
        })
    }

    private val _exerciseDetail = MutableStateFlow<ExerciseDetailDTO?>(null)
    val exerciseDetail: StateFlow<ExerciseDetailDTO?> = _exerciseDetail.asStateFlow()

    fun fetchExerciseDetails(exerciseId: Long, exerciseGoal: Int, intensity: Int) {
        Log.d(
            "ExerciseViewModel",
            "Fetching details for exerciseId: $exerciseId, exerciseGoal: $exerciseGoal, intensity: $intensity"
        )
        val apiService = RetrofitClient.instance

        apiService.getExerciseByGoalAndIntensity(exerciseId, exerciseGoal, intensity)
            .enqueue(object : Callback<ExerciseDTO> {
                override fun onResponse(call: Call<ExerciseDTO>, response: Response<ExerciseDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let { exercise ->
                            val detail =
                                exercise.detailList.firstOrNull() // ✅ Lấy detail đầu tiên nếu có
                            _exerciseDetail.value = detail // ✅ Cập nhật dữ liệu
                        }
                    } else {
                        Log.e("ExerciseViewModel", "API Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ExerciseDTO>, t: Throwable) {
                    Log.e("ExerciseViewModel", "Network Error", t)
                }
            })
    }

    private val _createdExercise = MutableLiveData<ExerciseDTO?>()
    val createdExercise: LiveData<ExerciseDTO?> get() = _createdExercise

    fun createExercise(exerciseDTO: ExerciseDTO) {
        val apiService = RetrofitClient.instance

        Log.d("ExerciseViewModel", "Creating exercise: $exerciseDTO") // ✅ Log dữ liệu gửi đi

        apiService.createExercise(exerciseDTO).enqueue(object : Callback<ExerciseDTO> {
            override fun onResponse(call: Call<ExerciseDTO>, response: Response<ExerciseDTO>) {
                if (response.isSuccessful) {
                    /*_createdExercise.value = response.body()
                    Log.d("ExerciseViewModel", "Exercise created: ${response.body()}")*/
                    response.body()?.let { newExercise ->
                        _createdExercise.value = newExercise
                        Log.d("ExerciseViewModel", "Exercise created: $newExercise")

                        val updatedList = _exerciseList.value.toMutableList()
                        updatedList.add(newExercise) // Thêm bài tập mới vào danh sách
                        _exerciseList.value = updatedList // Cập nhật danh sách
                    }
                } else {
                    Log.e("ExerciseViewModel", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ExerciseDTO>, t: Throwable) {
                Log.e("ExerciseViewModel", "Failed to create exercise", t)
            }
        })
    }

    fun deleteExercise(exerciseId: Long) {
        val apiService = RetrofitClient.instance

        apiService.deleteExercise(exerciseId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("ExerciseViewModel", "Delete response: $response")
                if (response.isSuccessful) {
                    Log.d("ExerciseViewModel", "Exercise deleted successfully: $exerciseId")
                    val updatedList = _exerciseList.value.filterNot { it.exerciseId == exerciseId }
                    _exerciseList.value = updatedList.toList()
                    Log.d("ExerciseViewModel", "Deleted exerciseId: $exerciseId")
                } else {
                    Log.e("ExerciseViewModel", "Delete error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ExerciseViewModel", "Delete failed", t)
            }
        })
    }

    private val _exercise = MutableStateFlow<ExerciseDTO?>(null)
    val exercise: StateFlow<ExerciseDTO?> = _exercise

    fun updateExercise(updatedExercise: ExerciseDTO) {
        RetrofitClient.instance.updateExercise(updatedExercise.exerciseId, updatedExercise)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("ExerciseViewModel", "Exercise updated successfully")
                        _exercise.value = updatedExercise
                    } else {
                        Log.e("ExerciseViewModel", "Update Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("ExerciseViewModel", "Update Failed", t)
                }
            })
    }

    private val _createdExerciseDone = MutableLiveData<ExerciseDoneDTO?>()
    val createdExerciseDone: LiveData<ExerciseDoneDTO?> get() = _createdExerciseDone

    private val _exerciseDoneList = MutableStateFlow<List<ExerciseDoneDTO>>(emptyList())
    val exerciseDoneList: StateFlow<List<ExerciseDoneDTO>> = _exerciseDoneList.asStateFlow()

    fun createExerciseDone(exerciseDoneDTO: ExerciseDoneDTO) {
        val apiService = RetrofitClient.instance

        apiService.createExerciseDone(exerciseDoneDTO).enqueue(object : Callback<ExerciseDoneDTO> {
            override fun onResponse(
                call: Call<ExerciseDoneDTO>,
                response: Response<ExerciseDoneDTO>
            ) {
                if (response.isSuccessful) {
                    /*_createdExercise.value = response.body()
                    Log.d("ExerciseViewModel", "Exercise created: ${response.body()}")*/
                    response.body()?.let { newExerciseDone ->
                        _createdExerciseDone.value = newExerciseDone
                        Log.d("ExerciseViewModel", "Exercise created: $newExerciseDone")

                        val updatedList = _exerciseDoneList.value.toMutableList()
                        updatedList.add(newExerciseDone) // Thêm bài tập mới vào danh sách
                        _exerciseDoneList.value = updatedList // Cập nhật danh sách
                    }
                } else {
                    Log.e("ExerciseViewModel", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ExerciseDoneDTO>, t: Throwable) {
                Log.e("ExerciseViewModel", "Failed to create exercise done", t)
            }
        })
    }

    private val _overviewExerciseList = MutableStateFlow<List<OverviewExerciseDTO>>(emptyList())
    val overviewExerciseList: StateFlow<List<OverviewExerciseDTO>> =
        _overviewExerciseList.asStateFlow()

    fun fetchOverviewExercises(context: Context, userId: Long, startDate: String, endDate: String) {
        RetrofitClient.instance.getOverviewExercises(userId, startDate, endDate)
            .enqueue(object : Callback<List<OverviewExerciseDTO>> {
                override fun onResponse(
                    call: Call<List<OverviewExerciseDTO>>,
                    response: Response<List<OverviewExerciseDTO>>
                ) {
                    if (response.isSuccessful) {
                        _overviewExerciseList.value = response.body() ?: emptyList()
                        Log.d("OverviewExerciseVM", "Overview exercises: ${response.body()}")

                        OverviewExerciseSharedPrefsHelper.saveListOverviewExercise(context, _overviewExerciseList.value.toList())
                    } else {
                        Log.e("OverviewExerciseVM", "API Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<OverviewExerciseDTO>>, t: Throwable) {
                    Log.e("OverviewExerciseVM", "Network Error", t)
                }
            })
    }

    private val _burnedCalories = MutableLiveData<Float>()
    val burnedCalories: LiveData<Float> get() = _burnedCalories

    fun getBurnedCaloriesToday(userId: Long) {
        val apiService = RetrofitClient.instance
        apiService.getBurnedCaloriesToday(userId).enqueue(object : Callback<Float> {
            override fun onResponse(call: Call<Float>, response: Response<Float>) {
                if (response.isSuccessful) {
                    val calories = response.body() ?: 0f  // Xử lý trường hợp null
                    _burnedCalories.postValue(calories)
                    Log.d("ExerciseViewModel", "Burned calories today: $calories")
                } else {
                    Log.e(
                        "ExerciseViewModel",
                        "API Error: ${response.code()} - ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Float>, t: Throwable) {
                Log.e("ExerciseViewModel", "Network Error: ${t.message}", t)
            }
        })
    }

    private val _exerciseDoneTime = MutableLiveData<Float>()
    val exerciseDoneTime: LiveData<Float> get() = _exerciseDoneTime

    fun getExerciseDoneTimeToday(userId: Long) {
        val apiService = RetrofitClient.instance
        apiService.getExerciseDoneTimeToday(userId).enqueue(object : Callback<Float> {
            override fun onResponse(call: Call<Float>, response: Response<Float>) {
                if (response.isSuccessful) {
                    val exerciseDoneTime = response.body() ?: 0f  // Xử lý trường hợp null
                    _exerciseDoneTime.postValue(exerciseDoneTime)
                    Log.d("ExerciseViewModel", "Exercise done time: $exerciseDoneTime")
                } else {
                    Log.e(
                        "ExerciseViewModel",
                        "API Error: ${response.code()} - ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Float>, t: Throwable) {
                Log.e("ExerciseViewModel", "Network Error: ${t.message}", t)
            }
        })
    }
}