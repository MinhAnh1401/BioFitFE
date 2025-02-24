package com.example.biofit.model

import android.content.Context
import android.util.Log
import com.example.biofit.R
import com.example.biofit.controller.DatabaseHelper
import com.google.ai.client.generativeai.GenerativeModel

data class ChatMessage(val userMessage: String, val botResponse: String)

class ChatBotModel(private val context: Context, apiKey: String, private val databaseHelper: DatabaseHelper) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )
    private val chat = generativeModel.startChat()

    private val chatHistory = mutableListOf<ChatMessage>()

    suspend fun getBotResponse(userInput: String): String {
        return try {
            val userData = databaseHelper.getUserDataById(userId = 1)
            val enrichedInput = enrichInputWithUserData(userInput, userData)
            val fullConversation = chatHistory.joinToString("\n") {
                "User: ${it.userMessage}\nBot: ${it.botResponse}"
            } + "\nUser: $enrichedInput"
            val response = chat.sendMessage(fullConversation)
            val botReply = response.text ?: context.getString(R.string.sorry_i_don_t_understand)
            chatHistory.add(ChatMessage(userInput, botReply))
            botReply
        } catch (e: Exception) {
            "Lỗi khi lấy dữ liệu!"
        }
    }

    private fun enrichInputWithUserData(userInput: String, userData: UserData?): String {
        return if (userData != null) {
            """
            Your name is Bionix.
            You are an AI assistant specializing in health and nutrition management, integrated into the BioFit app.
            BioFit is a platform that helps users track and improve their personal health.
            Response Rules:
            If the user's question is related to health and nutrition, respond based on user data and provide useful advice.
            If the question is about the BioFit app, provide intelligent and detailed information about its features and usage.
            If the question is unrelated, politely decline and guide the user back to health, nutrition, or BioFit-related topics.
            
            If the user wants to log out of the BioFit application then reply to the user that press the log out button below.
            
            User data:
                - Full Name: ${userData.fullName}
                - Gender: ${getGenderString(userData.gender, context)}
                - Date of Birth: ${userData.dateOfBirth}
                - Height: ${userData.height} cm
                - Weight: ${userData.weight} kg
                - Target Weight: ${userData.targetWeight} kg

            User asks: $userInput
            """.trimIndent()
        } else {
            userInput
        }
    }
}
