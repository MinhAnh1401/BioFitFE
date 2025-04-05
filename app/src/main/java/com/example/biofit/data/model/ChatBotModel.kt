package com.example.biofit.data.model

import android.content.Context
import android.util.Log
import com.example.biofit.R
import com.example.biofit.data.model.dto.DailyLogDTO
import com.example.biofit.data.model.dto.OverviewExerciseDTO
import com.example.biofit.data.model.dto.UserDTO
import com.example.biofit.data.utils.OverviewExerciseSharedPrefsHelper
import com.example.biofit.view_model.ExerciseViewModel
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Protocol.Companion.get
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatMessage(val userMessage: String, val botResponse: String)

class ChatBotModel(
    private val userData: UserDTO,
    private val dailyLogData: DailyLogDTO,
    private val exerciseDone: List<String>?,
    private val context: Context,
    apiKey: String,
    /*private val googleApiKey: String,
    private val searchEngineId: String*/
) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = apiKey
    )
    private val chat = generativeModel.startChat()

    private val chatHistory = mutableListOf<ChatMessage>()
    private var isFirstConversation = true

    suspend fun getBotResponse(userInput: String): String {
        // Kiểm tra nếu người dùng yêu cầu tìm kiếm bài báo/video
        /*if (userInput.contains("bài báo") || userInput.contains("video") || userInput.contains("hướng dẫn")) {
            val searchQuery = "sức khoẻ dinh dưỡng ${userInput}"
            val searchResults = searchGoogleScholar(searchQuery)
            return if (searchResults.isNotEmpty()) {
                "Dưới đây là một số bài báo/video về sức khoẻ và dinh dưỡng:\n" + searchResults.joinToString(
                    "\n"
                )
            } else {
                "Xin lỗi, tôi không thể tìm thấy tài liệu nào về sức khoẻ và dinh dưỡng tại thời điểm này."
            }
        }*/

        val recentHistory = chatHistory.takeLast(10)
        val conversationContext = recentHistory.joinToString("\n") {
            "User: ${it.userMessage}\nBot: ${it.botResponse}"
        }

        val userData = userData
        val dailyLogData = dailyLogData
        /*val exerciseViewModel = ExerciseViewModel()
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        exerciseViewModel.fetchOverviewExercises(context, userData.userId, userData.createdAccount, today)
        val overviewExerciseData = OverviewExerciseSharedPrefsHelper.getListOverviewExercise(context)
        val mappedExercises = overviewExerciseData?.map { exercise ->
            val levelStr = when (exercise.level) {
                0 -> context.getString(R.string.amateur)
                1 -> context.getString(R.string.professional)
                else -> context.getString(R.string.unknown)
            }

            val intensityStr = when (exercise.intensity) {
                0 -> context.getString(R.string.low)
                1 -> context.getString(R.string.medium)
                2 -> context.getString(R.string.high)
                else -> context.getString(R.string.unknown)
            }

            val sessionStr = when (exercise.session) {
                0 -> context.getString(R.string.morning)
                1 -> context.getString(R.string.afternoon)
                2 -> context.getString(R.string.evening)
                else -> context.getString(R.string.unknown)
            }

            "(${context.getString(R.string.exercise)}: ${exercise.exerciseName}, ${context.getString(R.string.level)}: $levelStr, ${context.getString(R.string.intensity)}: $intensityStr, ${context.getString(R.string.time)}: ${exercise.time} ${context.getString(R.string.minutes)}, ${context.getString(R.string.burned_calories)}: ${exercise.burnedCalories} ${context.getString(R.string.kcal)}, ${context.getString(R.string.session)}: $sessionStr, ${context.getString(R.string.day)}: ${exercise.date})"
        }
        Log.d("Exercise Done List", "exerciseDone: $mappedExercises")*/
        val enrichedInput = enrichInputWithUserData(userInput, userData, dailyLogData, exerciseDone)

        val fullConversation = if (conversationContext.isNotBlank()) {
            "$conversationContext\nUser: $enrichedInput"
        } else {
            enrichedInput
        }

        return try {
            val response = chat.sendMessage(fullConversation)
            val botReply = response.text
                ?.replace("*", "")
                /*?.replace(Regex("(https?://\\S+)"), "<a href=\"$1\">$1</a>")*/ // Chuyển link thành HTML
                ?: context.getString(R.string.sorry_i_don_t_understand)
            chatHistory.add(ChatMessage(userInput, botReply))
            botReply
        } catch (e: Exception) {
            context.getString(R.string.error_while_retrieving_data)
        }
    }

    /*private suspend fun searchGoogleScholar(query: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
                val url = "https://www.googleapis.com/customsearch/v1?q=$encodedQuery&key=$googleApiKey&cx=$searchEngineId"
                val response = get(url) // Gửi yêu cầu HTTP GET
                val jsonResponse = JSONObject(response.text) // Phân tích chuỗi JSON từ phản hồi

                // Truy cập vào danh sách "items" trong phản hồi JSON
                val items = jsonResponse.getJSONArray("items")
                val results = mutableListOf<String>()

                // Duyệt qua các item và lấy title + link
                for (i in 0 until items.length()) {
                    val title = items.getJSONObject(i).getString("title")
                    val link = items.getJSONObject(i).getString("link")
                    results.add("$title: $link")
                }

                results // Trả về danh sách kết quả tìm kiếm
            } catch (e: Exception) {
                emptyList<String>() // Nếu có lỗi, trả về danh sách rỗng
            }
        }
    }*/

    private fun enrichInputWithUserData(
        userInput: String,
        userData: UserDTO,
        dailyLogData: DailyLogDTO,
        exerciseDone: List<String>?
    ): String {
        val introText = if (isFirstConversation) {
            isFirstConversation = false
            context.getString(R.string.bionix_tranning).trimIndent()
        } else {
            ""
        }
        return """ 
            $introText
            
            Current date time: ${
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                if (java.util.Locale.getDefault().language == "vi")
                    "EEEE, 'ngày' dd 'tháng' MM 'năm' yyyy, 'giờ' HH 'phút' mm"
                else
                    "EEEE, MMMM d, yyyy, 'at' HH:mm"
            ))
        }
        
            [User data]:
                - Full Name: ${userData.fullName}
                - Gender: ${userData.getGenderString(context, userData.gender)}
                - Date of Birth: ${userData.dateOfBirth}
                - Height: ${userData.height} cm
                - Starting Weight: ${userData.weight} kg on ${userData.createdAccount}
                - Current Weight: ${dailyLogData.weight} kg
                - Drank ${dailyLogData.water} L of water today.
                - Target Weight: ${userData.targetWeight} kg
                - Completed Exercises: $exerciseDone
                
            [User input]: $userInput
        """.trimIndent()
    }
}
