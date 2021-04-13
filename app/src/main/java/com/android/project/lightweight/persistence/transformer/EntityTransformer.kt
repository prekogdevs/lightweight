package com.android.project.lightweight.persistence.transformer

import com.android.project.lightweight.api.retrofit.model.FoodNutrient
import com.android.project.lightweight.persistence.entity.NutrientEntry

object EntityTransformer {
    fun transformFoodNutrientsToNutrientEntries(consumedOn: Long, foodNutrients: List<FoodNutrient>) =
        // When this happens DiaryEntry not exists in DB this is why diaryEntryId is 0.
        foodNutrients.map { foodNutrient ->
            NutrientEntry(0, consumedOn, foodNutrient.nutrientNumber, foodNutrient.amount, foodNutrient.unitName, foodNutrient.nutrientName)
        }.toList()
}