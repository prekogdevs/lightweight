package com.android.project.lightweight.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.project.lightweight.persistence.entity.Food

@Dao
interface DiaryDao {
    @Query("SELECT * FROM food WHERE consumedOn = :consumedOn")
    fun getEntries(consumedOn: Long): LiveData<List<Food>>

    @Insert
    suspend fun addEntry(food : Food)

    @Delete
    suspend fun removeEntry(food : Food)
}