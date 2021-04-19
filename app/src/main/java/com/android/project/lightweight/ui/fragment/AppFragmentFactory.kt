package com.android.project.lightweight.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.android.project.lightweight.data.adapter.DiaryEntryAdapter
import com.android.project.lightweight.data.adapter.FoodAdapter
import com.android.project.lightweight.data.adapter.NutrientAdapter
import javax.inject.Inject

class AppFragmentFactory @Inject constructor(
    private val foodAdapter: FoodAdapter,
    private val diaryEntryAdapter: DiaryEntryAdapter,
    private val nutrientAdapter: NutrientAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            SearchFragment::class.java.name -> SearchFragment(foodAdapter)
            DiaryFragment::class.java.name -> DiaryFragment(diaryEntryAdapter)
            DetailsFragment::class.java.name -> DetailsFragment(nutrientAdapter)
            else -> return super.instantiate(classLoader, className)
        }
    }
}