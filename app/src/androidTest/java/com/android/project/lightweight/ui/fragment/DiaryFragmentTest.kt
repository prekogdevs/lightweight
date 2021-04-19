package com.android.project.lightweight.ui.fragment

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.android.project.lightweight.R
import com.android.project.lightweight.data.adapter.DiaryEntryAdapter
import com.android.project.lightweight.launchFragmentInHiltContainer
import com.android.project.lightweight.persistence.entity.DiaryEntry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DiaryFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickOnDiaryEntryInRecyclerView_navigateToDetailsFragment() {
        val navController = Mockito.mock(NavController::class.java)
        val diaryEntry = DiaryEntry(100, "TEST-DIARY-ENTRY", 20201010, 0, 0.0)
        launchFragmentInHiltContainer<DiaryFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            diaryEntryAdapter.submitList(listOf(diaryEntry))
        }
        Espresso.onView(ViewMatchers.withId(R.id.diary_recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<DiaryEntryAdapter.DiaryEntryHolder>(
                0, ViewActions.click()
            )
        )
        Mockito.verify(navController).navigate(DiaryFragmentDirections.actionDiaryFragmentToDetailsFragment(diaryEntry))
    }


}