package com.example.biofit.data.model.dto

data class OverviewExerciseDTO(
    val exerciseName: String,
    val level: Int,
    val intensity: Int,
    val time: Float,
    val burnedCalories: Float,
    val date: String,
    val session: Int
)