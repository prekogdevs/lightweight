package com.android.project.lightweight.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.project.lightweight.persistence.entity.DiaryEntry

@Dao
interface DiaryDao {
    @Query("SELECT * FROM Diary WHERE consumedOn = :consumedOn")
    fun getEntries(consumedOn: Long): LiveData<List<DiaryEntry>>

    @Insert
    suspend fun insertDiaryEntry(diaryEntry: DiaryEntry) : Long

    @Query("DELETE FROM Diary WHERE id = :diaryEntryId")
    suspend fun deleteDiaryEntry(diaryEntryId : Long)
}