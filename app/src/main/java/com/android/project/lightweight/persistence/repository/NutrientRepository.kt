package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.persistence.dao.NutrientDao
import com.android.project.lightweight.persistence.entity.NutrientEntry

class NutrientRepository(private val nutrientDao: NutrientDao) {
    suspend fun insertNutrientEntries(nutrientEntries : List<NutrientEntry>) = nutrientDao.insertNutrientEntries(nutrientEntries)
}