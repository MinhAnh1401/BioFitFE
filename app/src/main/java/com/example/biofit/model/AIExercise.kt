package com.example.biofit.model

data class AIExercise(
    val id: Int,
    val userPlanId: Int,
    val session: Int,
    val exerciseName: String,
    val duration: Int,
    val caloriesBurned: Float,
    val intensity: Int,
)