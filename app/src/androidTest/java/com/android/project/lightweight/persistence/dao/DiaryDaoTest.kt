package com.android.project.lightweight.persistence.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.android.project.lightweight.getOrAwaitValue
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DiaryDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DiaryDatabase
    private lateinit var dao: DiaryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DiaryDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.diaryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertDiaryEntryTest() = runBlockingTest {
        val diaryEntry = DiaryEntry(100, "Banana, raw", 23131231, 30, 200.0)
        dao.insertDiaryEntry(diaryEntry)

        val diaryEntries = dao.getEntries(23131231).getOrAwaitValue()
        assertThat(diaryEntries).contains(diaryEntry)
    }
}