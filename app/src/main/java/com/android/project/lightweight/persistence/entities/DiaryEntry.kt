package com.android.project.lightweight.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary")
class DiaryEntry(
    @PrimaryKey
    val CD_ID: String, // CD: Consumption date (e.g.: 2020-07-12)
    @ColumnInfo
    val fdcId : Long // Food ID
)