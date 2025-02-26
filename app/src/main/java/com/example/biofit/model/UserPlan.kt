package com.example.biofit.model

data class UserPlan(
    val id: Int,
    val userId: Int,
    val goal: Int,
    val planDuration: Int,
    val diet: Int,
    val workoutIntensity: Int,
    val status: Int,
)