package com.android.project.lightweight.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.relations.DiaryEntryWithNutrients

@Dao
interface DiaryDao {
    @Query("SELECT * FROM Diary WHERE consumedOn = :consumedOn")
    fun getEntries(consumedOn: Long): LiveData<List<DiaryEntry>>

    @Insert
    suspend fun insertDiaryEntry(diaryEntry: DiaryEntry): Long

    @Query("DELETE FROM Diary WHERE id = :diaryEntryId")
    suspend fun deleteDiaryEntry(diaryEntryId: Long)

    @Transaction
    @Query("SELECT * FROM Diary WHERE id = :id")
    fun getDiaryEntryWithNutrients(id: Long): LiveData<DiaryEntryWithNutrients>
}