package com.android.project.lightweight.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.project.lightweight.data.DetailsViewModel
import com.android.project.lightweight.network.Food

class ViewModelFactory(private val application: Application, private val food : Food) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(application, food) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}