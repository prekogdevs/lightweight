package com.android.project.lightweight.persistence

import com.android.project.lightweight.api.model.Food
import com.android.project.lightweight.persistence.entity.DiaryEntry

object DiaryEntryTransformer {
    fun transformEntryToFood(entry : DiaryEntry) = Food(entry.id, entry.description, entry.foodNutrients)
}