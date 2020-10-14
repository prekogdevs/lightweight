package com.android.project.lightweight.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary")
class DiaryEntry(
    @PrimaryKey
    val CD_ID: Long, // CD: Consumption date (e.g.: 20200712)
    @ColumnInfo
    val fdcId: Long,
    @ColumnInfo
    val foodName: String, // this is equal to description in network.Food
)