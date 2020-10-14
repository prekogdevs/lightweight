package com.android.project.lightweight.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.project.lightweight.persistence.entities.DiaryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [DiaryEntry::class], version = 1)
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao

    private class AddSampleDataCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { diaryDatabase ->
                scope.launch {
                    populateDatabase(diaryDatabase.diaryDao())
                }
            }
        }

        suspend fun populateDatabase(diaryDao: DiaryDao) {
            diaryDao.addEntry(DiaryEntry(20200701, 1124, "Banana"))
            diaryDao.addEntry(DiaryEntry(20200701, 15542, "Oats"))
            diaryDao.addEntry(DiaryEntry(20200701, 11643, "Apple"))
            diaryDao.addEntry(DiaryEntry(20200701, 11123, "Water"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        operator fun invoke(context: Context, scope: CoroutineScope) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, scope).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context, scope: CoroutineScope) =
            Room.databaseBuilder(context.applicationContext, DiaryDatabase::class.java, "diary_database").addCallback(AddSampleDataCallback(scope)).build()
    }
}