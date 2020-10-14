package com.android.project.lightweight.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.project.lightweight.data.DetailsViewModel
import com.android.project.lightweight.data.DiaryViewModel
import com.android.project.lightweight.api.model.Food

// TODO: Refactor later
class ViewModelFactory(private val application: Application, private val food : Food = Food(-1,"UNKNOWN", emptyList()), private val consumptionDate : Long) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(application, food, consumptionDate) as T
        }
        else if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(application, consumptionDate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}