package com.android.project.lightweight.persistence.repository

import androidx.lifecycle.LiveData
import com.android.project.lightweight.persistence.dao.DiaryDao
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.relations.DiaryEntryWithNutrients
import javax.inject.Inject

class DiaryRepository @Inject constructor(private val diaryDao: DiaryDao) : AbstractDiaryRepository {
    override suspend fun insertDiaryEntry(diaryEntry: DiaryEntry) = diaryDao.insertDiaryEntry(diaryEntry)
    override suspend fun deleteDiaryEntry(diaryEntryId: Long) = diaryDao.deleteDiaryEntry(diaryEntryId)
    override fun getEntries(consumedOn: Long) = diaryDao.getEntries(consumedOn)
    override fun getDiaryEntryWithNutrients(id: Long): LiveData<DiaryEntryWithNutrients> = diaryDao.getDiaryEntryWithNutrients(id)
}