package com.android.project.lightweight.persistence

import com.android.project.lightweight.persistence.entity.Food

// TODO: consumedWhen jelenleg nincs használva
//  fog ez kelleni később?
class DiaryRepository(private val diaryDao: DiaryDao) {

    suspend fun addEntry(food: Food) = diaryDao.addEntry(food)
    suspend fun removeEntry(food: Food) = diaryDao.removeEntry(food)
}