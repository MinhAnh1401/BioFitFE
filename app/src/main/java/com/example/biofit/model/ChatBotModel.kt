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
            val userData = databaseHelper.getUserData()
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
            You are a health and nutrition management assistant.
            Your name is Bionix
            
            - If the user's question is health-related, answer based on user data.
            - If not, politely guide them back to health-related topics.
            
            User data:
                - Name: ${userData.name}
                - Interests: ${userData.hobbies}
                - Weight: ${userData.weight} kg
                - Height: ${userData.height} cm

            User asks: $userInput
            """.trimIndent()
        } else {
            userInput
        }
    }
}
