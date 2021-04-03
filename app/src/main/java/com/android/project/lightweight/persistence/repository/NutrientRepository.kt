package com.android.project.lightweight.persistence.repository

import androidx.lifecycle.LiveData
import com.android.project.lightweight.persistence.dao.NutrientDao
import com.android.project.lightweight.persistence.entity.NutrientEntry
import javax.inject.Inject

class NutrientRepository @Inject constructor(private val nutrientDao: NutrientDao) : AbstractNutrientRepository {
    override suspend fun insertNutrientEntries(nutrientEntries: List<NutrientEntry>) = nutrientDao.insertNutrientEntries(nutrientEntries)
    override fun getNutrientEntriesByDiaryEntryId(diaryEntryId: Long): LiveData<List<NutrientEntry>> = nutrientDao.getNutrientEntriesByDiaryEntryId(diaryEntryId)
    override fun sumConsumedAmountByNutrients(consumedOn: Long, nutrientNumber: Int): LiveData<Double> = nutrientDao.sumConsumedAmountByNutrient(consumedOn, nutrientNumber)
}