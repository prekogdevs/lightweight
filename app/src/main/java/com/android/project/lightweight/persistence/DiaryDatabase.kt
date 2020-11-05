package com.android.project.lightweight.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.project.lightweight.persistence.dao.DiaryDao
import com.android.project.lightweight.persistence.dao.NutrientDao
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry

@Database(entities = [DiaryEntry::class, NutrientEntry::class], version = 1)
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao
    abstract fun nutrientDao() : NutrientDao

    companion object {
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        operator fun invoke(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, DiaryDatabase::class.java, "diary_database").build()
    }
}