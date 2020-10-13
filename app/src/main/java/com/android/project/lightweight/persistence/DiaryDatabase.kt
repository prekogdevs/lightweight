package com.android.project.lightweight.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.project.lightweight.persistence.entities.DiaryEntry
import com.android.project.lightweight.persistence.entities.FoodEntry

@Database(entities = [DiaryEntry::class, FoodEntry::class], version = 1)
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao

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