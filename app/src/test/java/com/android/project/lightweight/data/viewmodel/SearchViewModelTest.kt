package com.android.project.lightweight.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.project.lightweight.MainCoroutineRule
import com.android.project.lightweight.getOrAwaitValueTest
import com.android.project.lightweight.persistence.repository.FakeSearchRepository
import com.android.project.lightweight.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    private lateinit var searchViewModel: SearchViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        searchViewModel = SearchViewModel(FakeSearchRepository())
    }

    @Test
    fun `test when given query is not valid`() {
        // GIVEN
        // WHEN
        searchViewModel.searchForFood("wrong test query")

        //THEN
        assertThat(searchViewModel.foodResponse.getOrAwaitValueTest().status).isEqualTo(Status.ERROR)
    }


}