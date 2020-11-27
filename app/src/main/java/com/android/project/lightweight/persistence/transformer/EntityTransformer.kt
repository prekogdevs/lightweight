package com.android.project.lightweight.persistence.transformer

import com.android.project.lightweight.api.model.FoodNutrient
import com.android.project.lightweight.persistence.entity.NutrientEntry

object EntityTransformer {
    fun transformFoodNutrientsToNutrientEntries(foodNutrients: List<FoodNutrient>, diaryEntryId: Long) =
        // TODO: foodNutrient.amount is NOT EQUAL TO NutrientEntry.consumedAmount - change it later
        foodNutrients.map {
                foodNutrient ->
            NutrientEntry(diaryEntryId, foodNutrient.nutrientNumber, foodNutrient.amount, foodNutrient.unitName, foodNutrient.nutrientName)
        }.toList()
}