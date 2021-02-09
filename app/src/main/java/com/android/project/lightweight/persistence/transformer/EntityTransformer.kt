package com.android.project.lightweight.persistence.transformer

import com.android.project.lightweight.api.retrofit.model.FoodNutrient
import com.android.project.lightweight.persistence.entity.NutrientEntry

object EntityTransformer {
    fun transformFoodNutrientsToNutrientEntries(foodNutrients: List<FoodNutrient>, diaryEntryId: Long) =
        foodNutrients.map {
                foodNutrient ->
            NutrientEntry(diaryEntryId, foodNutrient.nutrientNumber, foodNutrient.amount, foodNutrient.unitName, foodNutrient.nutrientName)
        }.toList()
}