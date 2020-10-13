package com.android.project.lightweight.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FoodEntry(
    @PrimaryKey
    val fdcId: Long,
    @ColumnInfo
    val foodName: String, // this is equal to description in network.Food
)