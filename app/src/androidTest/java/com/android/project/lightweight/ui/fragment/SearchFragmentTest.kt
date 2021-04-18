package com.android.project.lightweight.ui.fragment

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.android.project.lightweight.FakeSearchRepositoryAndroidTest
import com.android.project.lightweight.R
import com.android.project.lightweight.api.retrofit.model.Food
import com.android.project.lightweight.data.adapter.FoodAdapter
import com.android.project.lightweight.data.viewmodel.SearchViewModel
import com.android.project.lightweight.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class SearchFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickFoodOnSearchFragment_navigateToDetailsFragment() {
        val food = Food(999, "TEST-FOOD", listOf())
        val navController = mock(NavController::class.java)
        val searchViewModel = SearchViewModel(FakeSearchRepositoryAndroidTest())
        launchFragmentInHiltContainer<SearchFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            foodAdapter.submitList(listOf(food))
        }
        onView(withId(R.id.foodRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FoodAdapter.FoodHolder>(
                0, click()
            )
        )
        // After the click on an element, the selected Food will be transformed into DiaryEntry
        // So navigate must be called with this transformed entity
        val diaryEntry = searchViewModel.createDiaryEntryFromFood(food)
        verify(navController).navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(diaryEntry))
    }

}