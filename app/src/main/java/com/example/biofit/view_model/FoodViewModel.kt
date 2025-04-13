package com.example.biofit.view_model

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biofit.R
import com.example.biofit.data.model.dto.FoodDTO
import com.example.biofit.data.model.dto.FoodDoneDTO
import com.example.biofit.data.model.dto.FoodSummaryDTO
import com.example.biofit.data.remote.RetrofitClient
import com.example.biofit.ui.screen.base64ToBitmap
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class FoodViewModel : ViewModel() {
    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    private val _foodList = MutableStateFlow<List<FoodDTO>>(emptyList())
    val foodList: StateFlow<List<FoodDTO>> = _foodList.asStateFlow()

    // Phương thức lấy danh sách thực phẩm
    fun fetchFood(userId: Long) {
        val apiService = RetrofitClient.instance

        apiService.getFood(userId).enqueue(object : Callback<List<FoodDTO>> {
            override fun onResponse(call: Call<List<FoodDTO>>, response: Response<List<FoodDTO>>) {
                if (response.isSuccessful) {
                    val foodList = response.body() ?: emptyList()
                    _foodList.value = foodList

                    // Log the size of the food list and the actual data
                    Log.d("FoodViewModel", "Fetched food list size: ${foodList.size}")
                    Log.d("FoodViewModel", "Fetched food list: $foodList")
                } else {
                    Log.e("FoodViewModel", "API Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<FoodDTO>>, t: Throwable) {
                Log.e("FoodViewModel", "Network Error", t)
            }
        })
    }



    private val _foodListCreate = MutableStateFlow<List<FoodDTO>>(emptyList())
    val foodListCreate: StateFlow<List<FoodDTO>> = _foodListCreate.asStateFlow()

    private val _createdFood = MutableLiveData<FoodDTO?>()
    val createdFood: LiveData<FoodDTO?> get() = _createdFood

    private val _foodImageBitmap = MutableStateFlow<Bitmap?>(null)
    val foodImageBitmap: StateFlow<Bitmap?> = _foodImageBitmap.asStateFlow()

    private val _createFoodResult = MutableStateFlow<Result<FoodDTO>?>(null)
    val createFoodResult: StateFlow<Result<FoodDTO>?> = _createFoodResult.asStateFlow()

    fun setFoodImage(bitmap: Bitmap) {
        _foodImageBitmap.value = bitmap
    }

    fun createFood(foodDTO: FoodDTO, context: Context) {
        // Chuyển FoodDTO thành JSON
        val gson = Gson()
        val foodJsonString = gson.toJson(foodDTO)
        val foodJson = RequestBody.create("application/json".toMediaType(), foodJsonString)

        // Chuyển Bitmap thành MultipartBody.Part
        val imagePart: MultipartBody.Part? = _foodImageBitmap.value?.let { bitmap ->
            val file = File(context.cacheDir, "food_image_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
            }
            val requestBody = RequestBody.create("image/*".toMediaType(), file)
            MultipartBody.Part.createFormData("image", file.name, requestBody)
        }

        val apiService = RetrofitClient.instance

        // Gọi API
        val call = apiService.createFood(foodJson, imagePart)
        call.enqueue(object : Callback<FoodDTO> {
            override fun onResponse(call: Call<FoodDTO>, response: Response<FoodDTO>) {
                if (response.isSuccessful) {
                    _createFoodResult.value = Result.success(response.body()!!)
                } else {
                    _createFoodResult.value = Result.failure(Exception("Failed to create food: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<FoodDTO>, t: Throwable) {
                _createFoodResult.value = Result.failure(t)
            }
        })
    }

    // Thêm phương thức để reset trạng thái
    fun resetCreateFoodResult() {
        _createFoodResult.value = null
    }

    fun deleteFood(foodId: Long) {
        val apiService = RetrofitClient.instance

        apiService.deleteFood(foodId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val updatedList = _foodList.value.filterNot { it.foodId == foodId }
                    _foodList.value = updatedList
                } else {
                    Log.e("FoodViewModel", "Delete error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FoodViewModel", "Delete failed", t)
            }
        })
    }
    private val _food = MutableStateFlow<FoodDTO?>(null)
    val food: StateFlow<FoodDTO?> = _food
    fun setFood(foodDTO: FoodDTO) {
        _food.value = foodDTO
    }

    private val _updateFoodResult = MutableStateFlow<Result<FoodDTO>?>(null)
    val updateFoodResult: StateFlow<Result<FoodDTO>?> = _updateFoodResult.asStateFlow()

    fun updateFood(foodDTO: FoodDTO, context: Context? = null) {
        val apiService = RetrofitClient.instance

        // Nếu không cần cập nhật hình ảnh, gọi API JSON đơn giản
        if (context == null || _foodImageBitmap.value == null) {
            apiService.updateFood(foodDTO.foodId, foodDTO).enqueue(object : Callback<FoodDTO> {
                override fun onResponse(call: Call<FoodDTO>, response: Response<FoodDTO>) {
                    if (response.isSuccessful) {
                        Log.d("FoodViewModel", "Food updated successfully: ${foodDTO.foodName}")
                        _updateFoodResult.value = Result.success(response.body()!!)
                    } else {
                        Log.e("FoodViewModel", "Error updating food: ${response.code()}")
                        _updateFoodResult.value = Result.failure(Exception("Error: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<FoodDTO>, t: Throwable) {
                    Log.e("FoodViewModel", "Failed to update food", t)
                    _updateFoodResult.value = Result.failure(t)
                }
            })
        } else {
            // Nếu cần cập nhật hình ảnh, dùng multipart/form-data
            val gson = Gson()
            val foodJsonString = gson.toJson(foodDTO)
            val foodJson = RequestBody.create("application/json".toMediaType(), foodJsonString)

            val imagePart: MultipartBody.Part? = _foodImageBitmap.value?.let { bitmap ->
                val file = File(context.cacheDir, "food_image_${System.currentTimeMillis()}.jpg")
                file.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    out.flush()
                }
                val requestBody = RequestBody.create("image/*".toMediaType(), file)
                MultipartBody.Part.createFormData("image", file.name, requestBody)
            }

            apiService.updateFoodWithImage(foodDTO.foodId, foodJson, imagePart).enqueue(object : Callback<FoodDTO> {
                override fun onResponse(call: Call<FoodDTO>, response: Response<FoodDTO>) {
                    if (response.isSuccessful) {
                        Log.d("FoodViewModel", "Food with image updated successfully: ${foodDTO.foodName}")
                        _updateFoodResult.value = Result.success(response.body()!!)
                    } else {
                        Log.e("FoodViewModel", "Error updating food with image: ${response.code()}")
                        _updateFoodResult.value = Result.failure(Exception("Error: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<FoodDTO>, t: Throwable) {
                    Log.e("FoodViewModel", "Failed to update food with image", t)
                    _updateFoodResult.value = Result.failure(t)
                }
            })
        }
    }

    fun resetUpdateFoodResult() {
        _updateFoodResult.value = null
    }

    fun setFoodList(foodItems: List<FoodDTO>) {
        _foodList.value = foodItems
    }
    fun getFoodBySession(session: String): List<FoodDTO> {
        return _foodList.value?.filter { it.session == session } ?: emptyList()
    }

    private val _createdFoodDone = MutableLiveData<FoodDoneDTO?>()
    val createdFoodDone: LiveData<FoodDoneDTO?> get() = _createdFoodDone

    private val _foodDoneList = MutableStateFlow<List<FoodDoneDTO>>(emptyList())
    val foodDoneList: StateFlow<List<FoodDoneDTO>> = _foodDoneList.asStateFlow()


    fun createFoodDone(foodDoneDTO: FoodDoneDTO) {
        Log.d("FoodViewModel", "Creating FoodDoneDTO: $foodDoneDTO")
        val apiService = RetrofitClient.instance

        apiService.createFoodDone(foodDoneDTO).enqueue(object : Callback<FoodDoneDTO> {
            override fun onResponse(
                call: Call<FoodDoneDTO>,
                response: Response<FoodDoneDTO>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { newFoodDone ->
                        _createdFoodDone.value = newFoodDone
                        Log.d("FoodViewModel", "Food created: $newFoodDone")

                        val updatedList = _foodDoneList.value.toMutableList()
                        updatedList.add(newFoodDone) // Thêm thực phẩm mới vào danh sách
                        _foodDoneList.value = updatedList // Cập nhật danh sách
                    }
                } else {
                    Log.e("FoodViewModel", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FoodDoneDTO>, t: Throwable) {
                Log.e("FoodViewModel", "Failed to create food done", t)
            }
        })
    }

    fun fetchFoodDoneList(userId: Long, date: String) {
        val apiService = RetrofitClient.instance
        Log.d("FoodViewModel", "🔍 Bắt đầu fetchFoodDoneList với userId=$userId và date=$date")

        apiService.getFoodDoneByDate(userId, date).enqueue(object : Callback<List<FoodDoneDTO>> {
            override fun onResponse(
                call: Call<List<FoodDoneDTO>>,
                response: Response<List<FoodDoneDTO>>
            ) {
                if (response.isSuccessful) {
                    val foodDoneList = response.body() ?: emptyList()
                    _foodDoneList.value = foodDoneList
                    Log.d("FoodViewModel", "✅ Đã lấy được foodDoneList (${foodDoneList.size} món): $foodDoneList")
                } else {
                    Log.e("FoodViewModel", "❌ API trả về lỗi: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FoodDoneDTO>>, t: Throwable) {
                Log.e("FoodViewModel", "🚨 Lỗi kết nối khi gọi API getFoodDoneByDate", t)
            }
        })
    }

    fun deleteFoodDone(foodDoneId: Long) {
        val apiService = RetrofitClient.instance

        apiService.deleteFoodDone(foodDoneId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Cập nhật danh sách sau khi xóa
                    val updatedList = _foodDoneList.value.filterNot { it.foodDoneId == foodDoneId }
                    _foodDoneList.value = updatedList
                    Log.d("FoodViewModel", "✅ Đã xóa FoodDone với id=$foodDoneId")
                } else {
                    Log.e("FoodViewModel", "❌ Lỗi xóa: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FoodViewModel", "🚨 Lỗi mạng khi xóa FoodDone", t)
            }
        })
    }

    private val _foodSummaryToday = MutableStateFlow<FoodSummaryDTO?>(null)
    val foodSummaryToday: StateFlow<FoodSummaryDTO?> = _foodSummaryToday.asStateFlow()

    private val _foodSummaryYesterday = MutableStateFlow<FoodSummaryDTO?>(null)
    val foodSummaryYesterday: StateFlow<FoodSummaryDTO?> = _foodSummaryYesterday.asStateFlow()

    fun getFoodSummary(userId: Long, date: String, isYesterday: Boolean = false) {
        val apiService = RetrofitClient.instance
        apiService.getFoodSummary(userId, date).enqueue(object : Callback<FoodSummaryDTO> {
            override fun onResponse(call: Call<FoodSummaryDTO>, response: Response<FoodSummaryDTO>) {
                if (response.isSuccessful) {
                    if (isYesterday) {
                        _foodSummaryYesterday.value = response.body()
                    } else {
                        _foodSummaryToday.value = response.body()
                    }
                    Log.d("FoodViewModel", "✅ Summary ${if (isYesterday) "hôm qua" else "hôm nay"}: ${response.body()}")
                } else {
                    Log.e("FoodViewModel", "❌ Lỗi lấy summary: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FoodSummaryDTO>, t: Throwable) {
                Log.e("FoodViewModel", "🚨 Lỗi mạng khi lấy summary", t)
            }
        })
    }
}
