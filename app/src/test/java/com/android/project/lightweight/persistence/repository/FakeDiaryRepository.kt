package com.android.project.lightweight.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.project.lightweight.persistence.entity.DiaryEntry

class FakeDiaryRepository : AbstractDiaryRepository {

    private val diaryEntries = mutableListOf<DiaryEntry>()
    private val observableDiaryEntries = MutableLiveData<List<DiaryEntry>>(diaryEntries)

    override suspend fun insertDiaryEntry(diaryEntry: DiaryEntry): Long {
        diaryEntries.add(diaryEntry)
        observableDiaryEntries.postValue(diaryEntries)
        return diaryEntry.id
    }

    override suspend fun deleteDiaryEntry(diaryEntryId: Long) {
        diaryEntries.removeIf { it.id == diaryEntryId }
        observableDiaryEntries.postValue(diaryEntries)
    }

    override fun getEntries(consumedOn: Long) = observableDiaryEntries

    override fun getDiaryEntryById(diaryEntryId: Long): LiveData<DiaryEntry> {
        TODO("Not yet implemented")
    }
}