package com.android.project.lightweight.persistence.transformer

import com.android.project.lightweight.api.model.Food
import com.android.project.lightweight.api.model.FoodNutrient
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry

object EntityTransformer {
    fun transformDiaryEntryToFood(diaryEntry: DiaryEntry) = Food(diaryEntry.fdcId, diaryEntry.description, emptyList())
    fun transformFoodNutrientsToNutrientEntries(foodNutrients: List<FoodNutrient>, diaryEntryId: Long) =
        // TODO: foodNutrient.amount is NOT EQUAL TO NutrientEntry.consumedAmount - change it later
        foodNutrients.map {
                foodNutrient ->
            NutrientEntry(diaryEntryId, foodNutrient.nutrientNumber, foodNutrient.amount, foodNutrient.unitName, foodNutrient.nutrientName)
        }.toList()

}