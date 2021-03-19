package com.android.project.lightweight.persistence.transformer

import com.android.project.lightweight.api.retrofit.model.FoodNutrient
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry

object EntityTransformer {
    fun transformFoodNutrientsToNutrientEntries(foodNutrients: List<FoodNutrient>, diaryEntry: DiaryEntry) =
        foodNutrients.map { foodNutrient ->
            NutrientEntry(diaryEntry.id, diaryEntry.consumedOn, foodNutrient.nutrientNumber, foodNutrient.amount, foodNutrient.unitName, foodNutrient.nutrientName)
        }.toList()
}