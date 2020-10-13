package com.android.project.lightweight.persistence

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.project.lightweight.network.Food

@Entity(tableName = "diary")
class DiaryEntry(
    @PrimaryKey
    val consumptionDate: String,
    @Embedded(prefix = "food_")
    val food: Food
)