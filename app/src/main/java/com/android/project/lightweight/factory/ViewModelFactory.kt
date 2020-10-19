package com.android.project.lightweight.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.project.lightweight.data.DetailsViewModel
import com.android.project.lightweight.data.DiaryViewModel
import com.android.project.lightweight.persistence.entity.Food

// TODO: Refactor later
class ViewModelFactory(private val application: Application, private val consumedWhen: Long = -1L, private val food: Food = Food(-1, "UNKNOWN", emptyList())) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(application, consumedWhen, food) as T
        } else if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(application, consumedWhen) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}