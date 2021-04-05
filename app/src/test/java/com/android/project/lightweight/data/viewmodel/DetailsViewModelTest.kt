package com.android.project.lightweight.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.project.lightweight.MainCoroutineRule
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.FakeDiaryRepository
import com.android.project.lightweight.persistence.repository.FakeNutrientRepository
import com.android.project.lightweight.util.AppConstants.energyNutrientNumber
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {
    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = DetailsViewModel(FakeDiaryRepository(), FakeNutrientRepository())
    }

    @Test
    fun testSaveDiaryEntry() {
        // GIVEN
        val diaryEntry = DiaryEntry(1234, "Banana, raw", 20200120, 100)
        diaryEntry.id = 1
        diaryEntry.nutrients = listOf(
            NutrientEntry(1, 20200120, energyNutrientNumber.toDouble(), 14.0, "G", "Carbs")
        )

        // WHEN
        viewModel.saveDiaryEntry(diaryEntry)

        // THEN

    }
}