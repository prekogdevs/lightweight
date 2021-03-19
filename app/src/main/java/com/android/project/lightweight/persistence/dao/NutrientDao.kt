package com.android.project.lightweight.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.project.lightweight.persistence.entity.NutrientEntry

@Dao
interface NutrientDao {
    @Insert
    suspend fun insertNutrientEntry(nutrientEntry: NutrientEntry)

    @Insert
    suspend fun insertNutrientEntries(nutrientEntries: List<NutrientEntry>)

    @Query("SELECT * FROM Nutrient WHERE diaryEntryId = :diaryEntryId")
    fun getNutrientEntriesByDiaryEntryId(diaryEntryId: Long): LiveData<List<NutrientEntry>>

    @Query("SELECT ROUND(SUM(consumedAmount),2) FROM Nutrient WHERE consumedOn = :consumedOn AND nutrientNumber = :nutrientNumber")
    fun sumConsumedAmountByNutrient(consumedOn: Long, nutrientNumber: Int) : LiveData<Double>
}