package com.android.project.lightweight.persistence.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.android.project.lightweight.getOrAwaitValue
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class DiaryDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("in_memory_database")
    lateinit var database: DiaryDatabase
    private lateinit var diaryDao: DiaryDao

    @Before
    fun setup() {
        hiltRule.inject()
        diaryDao = database.diaryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertDiaryEntryTest() = runBlockingTest {
        // GIVEN
        val diaryEntry = DiaryEntry(100, "Banana, raw", 20200120, 30, 200.0)

        // WHEN
        diaryDao.insertDiaryEntry(diaryEntry)

        // THEN
        val diaryEntries = diaryDao.getEntries(20200120).getOrAwaitValue()
        assertThat(diaryEntries).contains(diaryEntry)
    }

    @Test
    fun deleteDiaryEntryTest() = runBlockingTest {
        // GIVEN
        val diaryEntry = DiaryEntry(100, "Banana, raw", 20200120, 30, 200.0)

        // WHEN
        val insertedDiaryEntryID = diaryDao.insertDiaryEntry(diaryEntry)
        diaryDao.deleteDiaryEntry(insertedDiaryEntryID)

        // THEN
        val diaryEntries = diaryDao.getEntries(20200120).getOrAwaitValue()
        assertThat(diaryEntries).doesNotContain(diaryEntry)
    }
}