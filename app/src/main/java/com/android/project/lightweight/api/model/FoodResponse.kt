package com.android.project.lightweight.api.model

import com.android.project.lightweight.persistence.entity.Food

//  This will be returned from Retrofit query.
data class FoodResponse(
    val foods: List<Food>
)
