package com.android.project.lightweight.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.android.project.lightweight.MainCoroutineRule
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.FakeDiaryRepository
import com.android.project.lightweight.persistence.repository.FakeNutrientRepository
import com.android.project.lightweight.util.AppConstants.carbsNutrientNumber
import com.android.project.lightweight.util.AppConstants.energyNutrientNumber
import com.android.project.lightweight.util.AppConstants.fatsNutrientNumber
import com.android.project.lightweight.util.AppConstants.proteinNutrientNumber
import com.android.project.lightweight.util.FilterCategory
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

    private lateinit var diaryEntry: DiaryEntry

    @Before
    fun setup() {
        diaryEntry = DiaryEntry(1234, "Banana, raw", 20200120, 0, 0.0)
        diaryEntry.id = 1
        diaryEntry.nutrientEntries = listOf(
            NutrientEntry(diaryEntry.id, 20200120, energyNutrientNumber.toDouble(), 89.0, "G", "Energy"),
            NutrientEntry(diaryEntry.id, 20200120, proteinNutrientNumber.toDouble(), 1.09, "G", "Protein"),
            NutrientEntry(diaryEntry.id, 20200120, carbsNutrientNumber.toDouble(), 22.80, "G", "Carbs"),
            NutrientEntry(diaryEntry.id, 20200120, fatsNutrientNumber.toDouble(), 0.33, "G", "Fat"),
            NutrientEntry(diaryEntry.id, 20200120, 301.0, 5.0, "MG", "Calcium"),
            NutrientEntry(diaryEntry.id, 20200120, 303.0, 0.26, "MG", "Iron")
        )
        // this will hold and simulate a DiaryEntry instance
        // which is sent from DiaryFragment/SearchFragment to DetailsFragment
        val args = mutableMapOf<String, Any>()
        args["diaryEntry"] = diaryEntry
        detailsViewModel = DetailsViewModel(FakeDiaryRepository(), FakeNutrientRepository(), SavedStateHandle(args))
    }

    @Test
    fun `update diary entry should update diary entry fields`() {
        // GIVEN
        val consumptionAmount = 200
        val expectedNutrientEntries = listOf(
            NutrientEntry(diaryEntry.id, 20200120, energyNutrientNumber.toDouble(), 89.0, "G", "Energy", 178.0),
            NutrientEntry(diaryEntry.id, 20200120, proteinNutrientNumber.toDouble(), 1.09, "G", "Protein", 2.18),
            NutrientEntry(diaryEntry.id, 20200120, carbsNutrientNumber.toDouble(), 22.80, "G", "Carbs", 45.6),
            NutrientEntry(diaryEntry.id, 20200120, fatsNutrientNumber.toDouble(), 0.33, "G", "Fat", 0.66),
            NutrientEntry(diaryEntry.id, 20200120, 301.0, 5.0, "MG", "Calcium", 10.0),
            NutrientEntry(diaryEntry.id, 20200120, 303.0, 0.26, "MG", "Iron", 0.52)
        )
        val expectedConsumedCalories = 178

        // WHEN
        detailsViewModel.updateDiaryEntry(consumptionAmount)


        // THEN
        assertThat(diaryEntry.consumedAmount).isEqualTo(consumptionAmount)
        assertThat(diaryEntry.nutrientEntries).isEqualTo(expectedNutrientEntries)
        assertThat(diaryEntry.consumedCalories).isEqualTo(expectedConsumedCalories)
    }

    @Test
    fun `filter diary entry general nutrients should return nutrients only from general category`() {
        // GIVEN
        val expectedFilteredResult = listOf(
            NutrientEntry(diaryEntry.id, 20200120, energyNutrientNumber.toDouble(), 89.0, "G", "Energy"),
            NutrientEntry(diaryEntry.id, 20200120, proteinNutrientNumber.toDouble(), 1.09, "G", "Protein"),
            NutrientEntry(diaryEntry.id, 20200120, carbsNutrientNumber.toDouble(), 22.80, "G", "Carbs"),
            NutrientEntry(diaryEntry.id, 20200120, fatsNutrientNumber.toDouble(), 0.33, "G", "Fat"),
        )

        // WHEN
        val filteredNutrients = detailsViewModel.filterByNutrientCategory(FilterCategory.GENERAL)

        // THEN
        assertThat(filteredNutrients).isEqualTo(expectedFilteredResult)
    }
}