package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.persistence.dao.DiaryDao
import com.android.project.lightweight.persistence.entity.DiaryEntry

class DiaryRepository(private val diaryDao: DiaryDao) {
    suspend fun insertDiaryEntry(diaryEntry: DiaryEntry) : Long = diaryDao.insertDiaryEntry(diaryEntry)
    suspend fun deleteDiaryEntry(diaryEntryId : Long) = diaryDao.deleteDiaryEntry(diaryEntryId)
    fun getEntries(consumedOn: Long) = diaryDao.getEntries(consumedOn)
}