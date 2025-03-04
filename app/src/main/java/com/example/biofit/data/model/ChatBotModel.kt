package com.example.biofit.data.model

import android.content.Context
import com.example.biofit.R
import com.example.biofit.data.dto.UserDTO
import com.google.ai.client.generativeai.GenerativeModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatMessage(val userMessage: String, val botResponse: String)

class ChatBotModel(
    private val userData: UserDTO,
    private val context: Context,
    apiKey: String,
) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = apiKey
    )
    private val chat = generativeModel.startChat()

    private val chatHistory = mutableListOf<ChatMessage>()

    suspend fun getBotResponse(userInput: String): String {
        val recentHistory = chatHistory.takeLast(10)
        val conversationContext = recentHistory.joinToString("\n") {
            "User: ${it.userMessage}\nBot: ${it.botResponse}"
        }

        val userData = userData
        val enrichedInput = if (userData != null) {
            enrichInputWithUserData(userInput, userData)
        } else {
            userInput
        }

        val fullConversation = if (conversationContext.isNotBlank()) {
            "$conversationContext\nUser: $enrichedInput"
        } else {
            enrichedInput
        }

        return try {
            val response = chat.sendMessage(fullConversation)
            val botReply = response.text
                ?.replace("*", " ")
                ?: context.getString(R.string.sorry_i_don_t_understand)
            chatHistory.add(ChatMessage(userInput, botReply))
            botReply
        } catch (e: Exception) {
            context.getString(R.string.error_while_retrieving_data)
        }
    }

    private fun enrichInputWithUserData(userInput: String, userData: UserDTO): String {
        return """ 
            Current date time: ${
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
        
            User data:
                - Full Name: ${userData.fullName}
                - Gender: ${userData.getGenderString(context, userData.gender)}
                - Date of Birth: ${userData.dateOfBirth}
                - Height: ${userData.height} cm
                - Weight: ${userData.weight} kg
                - Target Weight: ${userData.targetWeight} kg
                
            User asks: $userInput
            
            Your name is Bionix.
            You are an AI assistant specializing in health and nutrition management, integrated into the BioFit app.
            BioFit is a application that helps users track and improve their personal health.
            Response Rules:
            If the user's question is related to health and nutrition, respond based on user data and provide useful advice.
            If the question is unrelated, politely decline and guide the user back to health, nutrition, or BioFit-related topics.
            Respond as detailed and concisely as possible and in the tone and context of a conversation like a close friend.
            """.trimIndent()
    }
}
