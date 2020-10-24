package com.android.project.lightweight.persistence

import com.android.project.lightweight.persistence.entity.Food

class DiaryRepository(private val diaryDao: DiaryDao, consumedWhen: Long) {

    suspend fun addEntry(food: Food) = diaryDao.addEntry(food)
}