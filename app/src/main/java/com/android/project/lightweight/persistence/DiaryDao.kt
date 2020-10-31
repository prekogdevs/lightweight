package com.android.project.lightweight.persistence

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
    suspend fun addEntry(diaryEntry: DiaryEntry)

    @Query("DELETE FROM Diary WHERE id = :foodId AND consumedOn = :consumedOn")
    suspend fun removeEntry(foodId: Long, consumedOn: Long)
}