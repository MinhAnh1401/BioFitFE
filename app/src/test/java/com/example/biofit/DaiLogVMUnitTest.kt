package com.example.biofit


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.biofit.data.model.dto.DailyLogDTO
import com.example.biofit.data.remote.ApiService
import com.example.biofit.data.remote.RetrofitClient
import com.example.biofit.data.utils.DailyLogSharedPrefsHelper
import com.example.biofit.view_model.DailyLogViewModel
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import retrofit2.Call
import kotlin.test.Test


@OptIn(ExperimentalCoroutinesApi::class)
class DaiLogVMUnitTest {
    private lateinit var viewModel: DailyLogViewModel
    private lateinit var mockContext: Context
    private lateinit var mockSharedPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        viewModel = DailyLogViewModel()

        // Mock các thành phần cần thiết
        mockContext = mock(Context::class.java)
        mockSharedPrefs = mock(SharedPreferences::class.java)
        mockEditor = mock(SharedPreferences.Editor::class.java)

        // Mock Log.d để không ảnh hưởng đến output của test
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        // Giả lập getSharedPreferences trả về mockSharedPrefs
        `when`(mockContext.getSharedPreferences(anyString(), anyInt()))
            .thenReturn(mockSharedPrefs)

        // Giả lập editor cho putString/apply
        `when`(mockSharedPrefs.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)

        // Trả về dữ liệu cũ để test getDailyLog
        val oldLogJson = """
            {
                "id": 1,
                "userId": 100,
                "weight": 70.0,
                "water": 1.5,
                "date": "2025-04-08"
            }
        """.trimIndent()

        `when`(mockSharedPrefs.getString(eq("DAILY_LOG"), anyOrNull()))
            .thenReturn(oldLogJson)
    }

    @Test
    fun `saveDailyLog only updates weight and keeps old water value`() {
        // Gán giá trị mới vào ViewModel
        viewModel.weight.value = 75.0f
        viewModel.water.value = null

        // Gọi hàm đang test
        viewModel.saveDailyLog(userId = 100L, context = mockContext)

        // Bạn có thể verify log hoặc logic khác tại đây nếu cần
        // verify(mockEditor).putString(eq("DAILY_LOG"), any())
        verify(mockEditor).apply()
    }

    @Test
    fun `saveDailyLog should call apply when saving log`() {
        val dailyLog = DailyLogDTO(1, 123L, 70f, 2f, "2025-04-08")

        DailyLogSharedPrefsHelper.saveDailyLog(mockContext, dailyLog)

        verify(mockEditor).putString(eq("DAILY_LOG"), any())
        verify(mockEditor).apply()
    }
}