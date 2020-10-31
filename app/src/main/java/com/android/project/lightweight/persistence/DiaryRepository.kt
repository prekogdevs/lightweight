package com.android.project.lightweight.persistence

import com.android.project.lightweight.persistence.entity.DiaryEntry

class DiaryRepository(private val diaryDao: DiaryDao) {
    suspend fun addEntry(diaryEntry: DiaryEntry) = diaryDao.addEntry(diaryEntry)
    suspend fun removeEntry(foodId: Long, consumedOn: Long) = diaryDao.removeEntry(foodId, consumedOn)
}