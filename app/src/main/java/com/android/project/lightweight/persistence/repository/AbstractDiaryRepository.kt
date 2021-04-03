package com.android.project.lightweight.persistence.repository

import androidx.lifecycle.LiveData
import com.android.project.lightweight.persistence.entity.DiaryEntry

interface AbstractDiaryRepository {
    suspend fun insertDiaryEntry(diaryEntry: DiaryEntry): Long
    suspend fun deleteDiaryEntry(diaryEntryId: Long)
    fun getEntries(consumedOn: Long): LiveData<List<DiaryEntry>>
}