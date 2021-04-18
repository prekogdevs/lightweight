package com.android.project.lightweight.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.project.lightweight.MainCoroutineRule
import com.android.project.lightweight.getOrAwaitValueTest
import com.android.project.lightweight.persistence.repository.FakeDiaryRepository
import com.android.project.lightweight.persistence.repository.FakeNutrientRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DiaryViewModelTest {
    private lateinit var diaryViewModel: DiaryViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        diaryViewModel = DiaryViewModel(FakeDiaryRepository(), FakeNutrientRepository())
    }

    @Test
    fun `test consumed foods size when diary is empty`() {
        val consumedFoods = diaryViewModel.consumedFoods
        assertThat(consumedFoods.getOrAwaitValueTest().size).isEqualTo(0)
    }


}