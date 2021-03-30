package com.android.project.lightweight.persistence.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.android.project.lightweight.getOrAwaitValue
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
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
class NutrientDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DiaryDatabase
    private lateinit var nutrientDao: NutrientDao
    private lateinit var diaryDao: DiaryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DiaryDatabase::class.java
        ).allowMainThreadQueries().build()
        nutrientDao = database.nutrientDao()
        diaryDao = database.diaryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNutrientEntryTest() = runBlockingTest {
        // GIVEN
        val diaryEntry = DiaryEntry(100, "Protein shake", 20200120, 30, 200.0)
        val diaryEntryId = diaryDao.insertDiaryEntry(diaryEntry)
        val nutrientEntry = NutrientEntry(diaryEntryId, 20200120, 203.0, 34.0, "g", "Protein")

        // WHEN
        nutrientDao.insertNutrientEntry(nutrientEntry)

        // THEN
        val nutrientEntries = nutrientDao.getNutrientEntriesByDiaryEntryId(1).getOrAwaitValue()
        assertThat(nutrientEntries).contains(nutrientEntry)
    }

    @Test
    fun insertNutrientEntriesTest() = runBlockingTest {
        // GIVEN
        val diaryEntry = DiaryEntry(100, "Fruit shake with protein powder and almonds", 20200120, 30, 200.0)
        val diaryEntryId = diaryDao.insertDiaryEntry(diaryEntry)
        val proteinEntry = NutrientEntry(diaryEntryId, 20200120, 205.0, 34.0, "g", "Protein")
        val carbohydrateEntry = NutrientEntry(diaryEntryId, 20200120, 204.0, 82.0, "g", "Carbohydrates")
        val fatEntry = NutrientEntry(diaryEntryId, 20200120, 203.0, 11.0, "g", "Fat")
        val energyEntry = NutrientEntry(diaryEntryId, 20200120, 208.0, 563.0, "KCAL", "Energy")
        val nutrientEntriesToInsert = listOf(proteinEntry, carbohydrateEntry, fatEntry, energyEntry)

        // WHEN
        nutrientDao.insertNutrientEntries(nutrientEntriesToInsert)

        // THEN
        val nutrientEntries = nutrientDao.getNutrientEntriesByDiaryEntryId(diaryEntryId).getOrAwaitValue()
        assertThat(nutrientEntries).isEqualTo(nutrientEntriesToInsert)
    }

    @Test
    fun sumConsumedAmountByNutrientTest() = runBlockingTest {
        // GIVEN
        val chocolateShake = DiaryEntry(100, "Chocolate shake", 20200120, 30, 250.0)
        val vanillaShake = DiaryEntry(101, "Vanilla shake", 20200120, 30, 200.0)
        val caramelShake = DiaryEntry(102, "Salted, maca caramel shake", 20200120, 30, 175.0)
        val chocolateShakeId = diaryDao.insertDiaryEntry(chocolateShake)
        val vanillaShakeId = diaryDao.insertDiaryEntry(vanillaShake)
        val caramelShakeId = diaryDao.insertDiaryEntry(caramelShake)
        val proteinInChocolateShake = NutrientEntry(chocolateShakeId, 20200120, 205.0, 32.0, "g", "Protein")
        val proteinInVanillaShake = NutrientEntry(vanillaShakeId, 20200120, 205.0, 30.0, "g", "Protein")
        val proteinInCaramelShake = NutrientEntry(caramelShakeId, 20200120, 205.0, 29.0, "g", "Protein")
        val nutrientEntriesToInsert = listOf(proteinInChocolateShake, proteinInVanillaShake, proteinInCaramelShake)

        // WHEN
        nutrientDao.insertNutrientEntries(nutrientEntriesToInsert)

        // THEN
        val proteinSumInShakes = nutrientDao.sumConsumedAmountByNutrient(20200120, 205).getOrAwaitValue()
        assertThat(proteinSumInShakes).isEqualTo(91.0)
    }
}