package com.example.biofit.data.model.dto

data class OverviewExerciseDTO(
    val exerciseName: String,
    val time: Float,
    val burnedCalories: Float,
    val date: String,
    val session: Int
)