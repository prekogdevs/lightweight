package com.android.project.lightweight.persistence

import com.android.project.lightweight.persistence.entities.DiaryEntry

class DiaryRepository(private val diaryDao: DiaryDao, consumptionDate: Long = 0L) {
    val entries = diaryDao.getEntries(consumptionDate)

    suspend fun addEntry(entry: DiaryEntry) = diaryDao.addEntry(entry)
}