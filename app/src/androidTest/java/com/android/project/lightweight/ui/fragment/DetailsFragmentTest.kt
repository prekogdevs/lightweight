package com.android.project.lightweight.ui.fragment

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.android.project.lightweight.R
import com.android.project.lightweight.launchFragmentInHiltContainer
import com.android.project.lightweight.persistence.entity.DiaryEntry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DetailsFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun saveNewDiaryEntry_saveDiaryMenuShouldBeVisible() {
        val testDiaryEntry = DiaryEntry(100, "TEST", 0L, 0, 0.0)
        val args = Bundle()
        args.putParcelable("diaryEntry", testDiaryEntry)
        launchFragmentInHiltContainer<DetailsFragment>(fragmentFactory = fragmentFactory, fragmentArgs = args)
        onView(withId(R.id.save_diary_menu)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun deleteExistingDiaryEntry_deleteDiaryMenuShouldBeVisible() {
        val testDiaryEntry = DiaryEntry(100, "TEST", 0L, 0, 0.0)
        testDiaryEntry.id = 1
        val args = Bundle()
        args.putParcelable("diaryEntry", testDiaryEntry)
        launchFragmentInHiltContainer<DetailsFragment>(fragmentFactory = fragmentFactory, fragmentArgs = args)
        onView(withId(R.id.delete_diary_menu)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}