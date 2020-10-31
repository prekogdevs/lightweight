package com.android.project.lightweight.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.project.lightweight.persistence.converter.FoodNutrientConverter
import com.android.project.lightweight.persistence.entity.DiaryEntry

@Database(entities = [DiaryEntry::class], version = 1)
@TypeConverters(FoodNutrientConverter::class)
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