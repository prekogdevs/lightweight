package com.android.project.lightweight.persistence.repository

import androidx.lifecycle.LiveData
import com.android.project.lightweight.persistence.dao.NutrientDao
import com.android.project.lightweight.persistence.entity.NutrientEntry

class NutrientRepository(private val nutrientDao: NutrientDao) {
    suspend fun insertNutrientEntries(nutrientEntries: List<NutrientEntry>) = nutrientDao.insertNutrientEntries(nutrientEntries)
    fun getNutrientEntriesByDiaryEntryId(diaryEntryId: Long): LiveData<List<NutrientEntry>> = nutrientDao.getNutrientEntriesByDiaryEntryId(diaryEntryId)
    fun sumConsumedAmountByNutrients(consumedOn: Long, nutrientNumber: Int): LiveData<Double> = nutrientDao.sumConsumedAmountByNutrient(consumedOn, nutrientNumber)
}