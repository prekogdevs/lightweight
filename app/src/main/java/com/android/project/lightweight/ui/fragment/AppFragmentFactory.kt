package com.android.project.lightweight.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.android.project.lightweight.data.adapter.DiaryEntryAdapter
import javax.inject.Inject

class AppFragmentFactory @Inject constructor(
    private val diaryEntryAdapter: DiaryEntryAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            DiaryFragment::class.java.name -> DiaryFragment()
            else -> return super.instantiate(classLoader, className)
        }
    }
}