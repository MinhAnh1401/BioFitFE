package com.example.biofit.data.model.dto

import com.example.biofit.R

data class FoodInfoDTO(
    val foodId: Long,
    val foodImage: Int,
    val foodName: String,
    val servingSize: Pair<Float, String>,
    val mass: Float,
    val calories: Float,
    val protein: Triple<Int, Int, Float>,
    val carbohydrate: Triple<Int, Int, Float>,
    val fat: Triple<Int, Int, Float>,
    val sodium: Float? = null,
    val session: String
) {
    companion object {
        fun default(): FoodInfoDTO {
            return FoodInfoDTO(
                foodId = 0,
                foodImage = R.drawable.img_food_default,  // Bạn có thể thay đổi ảnh mặc định ở đây
                foodName = "N/A",
                servingSize = Pair(0f, "g"),
                mass = 0f,
                calories = 0f,
                protein = Triple(R.drawable.ic_protein, R.string.protein, 0f),
                carbohydrate = Triple(R.drawable.ic_carbohydrate, R.string.carbohydrate, 0f),
                fat = Triple(R.drawable.ic_fat, R.string.fat, 0f),
                sodium = 0f,
                session = "morning"
            )
        }
    }
}