package com.android.project.lightweight.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.project.lightweight.MainCoroutineRule
import com.android.project.lightweight.getOrAwaitValueTest
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.FakeDiaryRepository
import com.android.project.lightweight.persistence.repository.FakeNutrientRepository
import com.android.project.lightweight.util.AppConstants.carbsNutrientNumber
import com.android.project.lightweight.util.AppConstants.energyNutrientNumber
import com.android.project.lightweight.util.AppConstants.fatsNutrientNumber
import com.android.project.lightweight.util.AppConstants.proteinNutrientNumber
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {
    private lateinit var detailsViewModel: DetailsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        detailsViewModel = DetailsViewModel(FakeDiaryRepository(), FakeNutrientRepository())
    }

    @Test
    fun `save diary entry with given nutrients test should pass`() {
        // GIVEN
        val diaryEntry = DiaryEntry(1234, "Banana, raw", 20200120, 100, 0.0)
        diaryEntry.id = 1
        diaryEntry.nutrientEntries = listOf(
            NutrientEntry(diaryEntry.id, 20200120, energyNutrientNumber.toDouble(), 14.0, "G", "Energy")
        )

        // WHEN
        detailsViewModel.saveDiaryEntry(diaryEntry)
        detailsViewModel.setDiaryEntry(diaryEntry)

        // THEN
        assertThat(detailsViewModel.nutrients.getOrAwaitValueTest()).isEqualTo(diaryEntry.nutrientEntries)
    }

    @Test
    fun `set diary entry consumed calories test should pass`() {
        // GIVEN
        val diaryEntry = DiaryEntry(1234, "Banana, raw", 20200120, 200, 0.0)
        diaryEntry.id = 1
        diaryEntry.nutrientEntries = listOf(
            NutrientEntry(diaryEntry.id, 20200120, energyNutrientNumber.toDouble(), 14.0, "G", "Energy")
        )
        val consumptionAmount = 200
        val expectedConsumedCalories = 28

        // WHEN
        detailsViewModel.updateDiaryEntry(diaryEntry, consumptionAmount)

        // THEN
        assertThat(diaryEntry.consumedCalories).isEqualTo(expectedConsumedCalories)

    }

    @Test
    fun `set diary entry consumed calories test should pass when expecting wrong value`() {
        // GIVEN
        val diaryEntry = DiaryEntry(1234, "Banana, raw", 20200120, 100, 0.0)
        diaryEntry.id = 1
        diaryEntry.nutrientEntries = listOf(
            NutrientEntry(diaryEntry.id, 20200120, energyNutrientNumber.toDouble(), 50.0, "G", "Energy")
        )
        val consumptionAmount = 32
        val consumedKCAL = 14

        // WHEN
        detailsViewModel.updateDiaryEntry(diaryEntry, consumptionAmount)

        // THEN
        assertThat(diaryEntry.consumedCalories).isNotEqualTo(consumedKCAL)

    }

    @Test
    fun `filter diary entry nutrients should pass`() {
        // GIVEN
        val diaryEntry = DiaryEntry(1234, "Banana, raw", 20200120, 100, 0.0)
        diaryEntry.id = 1
        diaryEntry.nutrientEntries = listOf(
            NutrientEntry(diaryEntry.id, 20200120, energyNutrientNumber.toDouble(), 5.0, "G", "Energy"),
            NutrientEntry(diaryEntry.id, 20200120, proteinNutrientNumber.toDouble(), 150.0, "G", "Protein"),
            NutrientEntry(diaryEntry.id, 20200120, carbsNutrientNumber.toDouble(), 30.0, "G", "Carbs"),
            NutrientEntry(diaryEntry.id, 20200120, fatsNutrientNumber.toDouble(), 22.0, "G", "Fat")
        )
        val expectedFilteredResult = listOf(NutrientEntry(diaryEntry.id, 20200120, proteinNutrientNumber.toDouble(), 150.0, "G", "Protein"))

        // WHEN
        val filteredNutrients = detailsViewModel.filter(diaryEntry.nutrientEntries, listOf(proteinNutrientNumber))

        // THEN
        assertThat(filteredNutrients).isEqualTo(expectedFilteredResult)
    }
}