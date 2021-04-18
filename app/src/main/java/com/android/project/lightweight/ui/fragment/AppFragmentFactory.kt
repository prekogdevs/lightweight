package com.android.project.lightweight.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.android.project.lightweight.data.adapter.FoodAdapter
import javax.inject.Inject

class AppFragmentFactory @Inject constructor(
    private val foodAdapter: FoodAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            SearchFragment::class.java.name -> SearchFragment(foodAdapter)
            else -> return super.instantiate(classLoader, className)
        }
    }
}