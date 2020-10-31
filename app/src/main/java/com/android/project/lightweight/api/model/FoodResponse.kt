package com.android.project.lightweight.api.model

import com.android.project.lightweight.persistence.entity.Food

// Returned from Retrofit query.
data class FoodResponse(
    val foods: List<Food>
)
