package com.android.project.lightweight.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.project.lightweight.network.Food
import com.android.project.lightweight.persistence.entities.DiaryEntry

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary WHERE CD_ID = :consumptionDate")
    fun getEntries(consumptionDate: Long): LiveData<List<DiaryEntry>>

    @Insert
    fun addEntry(entry : DiaryEntry)
}