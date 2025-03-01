package com.example.biofit.view_model

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.biofit.R
import com.example.biofit.data.model.ChatBotModel
import com.example.biofit.data.model.ChatMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AIChatbotViewModel(
    private val model: ChatBotModel,
    private val context: Context
) : ViewModel() {
    private val _chatHistory = mutableStateListOf<ChatMessage>()
    val chatHistory: List<ChatMessage> get() = _chatHistory

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    fun sendMessage(userInput: String, scope: CoroutineScope) {
        if (userInput.isBlank()) return

        _chatHistory.add(ChatMessage(userInput, context.getString(R.string.thinking)))
        _isLoading.value = true

        scope.launch {
            val response = model.getBotResponse(userInput)
            _chatHistory[_chatHistory.lastIndex] = ChatMessage(userInput, response)
            _isLoading.value = false
        }
    }
}
