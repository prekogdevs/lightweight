package com.android.project.lightweight.network

data class Food(
    val fdcId: Long,
    val description: String,
    val foodNutrients : List<FoodNutrient>
)
