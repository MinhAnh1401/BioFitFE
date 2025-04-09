package com.example.biofit

import android.content.Context
import com.example.biofit.data.model.dto.ExerciseDTO
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ExerciseVMUnitTest {
    private lateinit var mockContext: Context

    @Before
    fun setup() {
        mockContext = mock(Context::class.java)
        `when`(mockContext.getString(R.string.amateur)).thenReturn("Amateur")
        `when`(mockContext.getString(R.string.professional)).thenReturn("Professional")
        `when`(mockContext.getString(R.string.exercise_goal_not_provided)).thenReturn("Not Provided")

        `when`(mockContext.getString(R.string.low)).thenReturn("Low")
        `when`(mockContext.getString(R.string.medium)).thenReturn("Medium")
        `when`(mockContext.getString(R.string.high)).thenReturn("High")
        `when`(mockContext.getString(R.string.intensity_not_provided)).thenReturn("Not Provided")
    }

    @Test
    fun `getExerciseGoalString returns correct string`() {
        val dto = ExerciseDTO.default()
        assertEquals("Amateur", dto.getExerciseGoalString(mockContext, 0))
        assertEquals("Professional", dto.getExerciseGoalString(mockContext, 1))
        assertEquals("Not Provided", dto.getExerciseGoalString(mockContext, 99))
    }

    @Test
    fun `getExerciseGoalInt returns correct int`() {
        val dto = ExerciseDTO.default()
        assertEquals(0, dto.getExerciseGoalInt(mockContext, "Amateur"))
        assertEquals(1, dto.getExerciseGoalInt(mockContext, "Professional"))
        assertEquals(-1, dto.getExerciseGoalInt(mockContext, "Unknown"))
    }

    @Test
    fun `getIntensityString returns correct string`() {
        val dto = ExerciseDTO.default()
        assertEquals("Low", dto.getIntensityString(mockContext, 0))
        assertEquals("Medium", dto.getIntensityString(mockContext, 1))
        assertEquals("High", dto.getIntensityString(mockContext, 2))
        assertEquals("Not Provided", dto.getIntensityString(mockContext, 99))
    }

    @Test
    fun `getIntensityInt returns correct int`() {
        val dto = ExerciseDTO.default()
        assertEquals(0, dto.getIntensityInt(mockContext, "Low"))
        assertEquals(1, dto.getIntensityInt(mockContext, "Medium"))
        assertEquals(2, dto.getIntensityInt(mockContext, "High"))
        assertEquals(-1, dto.getIntensityInt(mockContext, "Unknown"))
    }

    @Test
    fun `default ExerciseDTO is correct`() {
        val dto = ExerciseDTO.default()
        assertEquals(0, dto.exerciseId)
        assertEquals(0, dto.userId)
        assertEquals("N/A", dto.exerciseName)
        assertTrue(dto.detailList.isEmpty())
    }
}