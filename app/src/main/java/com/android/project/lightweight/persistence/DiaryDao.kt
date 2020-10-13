package com.android.project.lightweight.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.android.project.lightweight.network.Food

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary WHERE consumptionDate = :consumptionDate")
    fun getFoods(consumptionDate: String): LiveData<List<Food>>
}