package com.android.project.lightweight.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.project.lightweight.persistence.dao.DiaryDao
import com.android.project.lightweight.persistence.dao.NutrientDao
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry

@Database(entities = [DiaryEntry::class, NutrientEntry::class], version = 1)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
    abstract fun nutrientDao(): NutrientDao
}