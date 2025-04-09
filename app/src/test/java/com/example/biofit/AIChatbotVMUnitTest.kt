package com.example.biofit

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.biofit.data.model.ChatBotModel
import com.example.biofit.view_model.AIChatbotViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AIChatbotVMUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AIChatbotViewModel
    private lateinit var chatBotModel: ChatBotModel
    private lateinit var context: Context
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        chatBotModel = mock()
        context = mock()
        sharedPrefs = mock()
        editor = mock()

        whenever(context.getString(R.string.composing_a_message)).thenReturn("Đang soạn tin...")
        whenever(context.getSharedPreferences("chat_prefs", Context.MODE_PRIVATE)).thenReturn(sharedPrefs)
        whenever(sharedPrefs.edit()).thenReturn(editor)
        whenever(editor.putString(any(), any())).thenReturn(editor)
        whenever(editor.remove(any())).thenReturn(editor)

        viewModel = AIChatbotViewModel(chatBotModel, context)
    }

    @Test
    fun `sendMessage should add user message and response`() = runTest {
        val userInput = "Xin chào"
        val botResponse = "Chào bạn!"

        whenever(chatBotModel.getBotResponse(userInput)).thenReturn(botResponse)

        viewModel.sendMessage(userInput, this)

        advanceUntilIdle()

        val chatHistory = viewModel.chatHistory
        assertEquals(1, chatHistory.size)
        assertEquals(userInput, chatHistory[0].userMessage)
        assertEquals(botResponse, chatHistory[0].botResponse)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `sendMessage should not send when input is blank`() = runTest {
        viewModel.sendMessage(" ", this)
        advanceUntilIdle()
        assertTrue(viewModel.chatHistory.isEmpty())
    }

    @Test
    fun `clearChatHistory should remove all messages`() {
        // Add fake history
        viewModel.clearChatHistory()
        verify(editor).remove("chat_history")
        verify(editor).apply()
        assertTrue(viewModel.chatHistory.isEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}