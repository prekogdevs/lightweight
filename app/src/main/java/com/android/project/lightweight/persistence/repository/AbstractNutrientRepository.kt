package com.android.project.lightweight.persistence.repository

import androidx.lifecycle.LiveData
import com.android.project.lightweight.persistence.entity.NutrientEntry

interface AbstractNutrientRepository {
    suspend fun insertNutrientEntries(nutrientEntries: List<NutrientEntry>)
    fun getNutrientEntriesByDiaryEntryId(diaryEntryId: Long): LiveData<List<NutrientEntry>>
    fun sumConsumedAmountByNutrients(consumedOn: Long, nutrientNumber: Int): LiveData<Double>
}