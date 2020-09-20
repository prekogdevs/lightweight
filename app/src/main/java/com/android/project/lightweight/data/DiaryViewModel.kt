package com.android.project.lightweight.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.project.lightweight.network.Food

class DiaryViewModel : ViewModel() {
    private val _consumedFoods = MutableLiveData<ArrayList<Food>>()
    val consumedFoods: LiveData<ArrayList<Food>>
        get() = _consumedFoods

    init {
        // This will come from Room DB later
        _consumedFoods.value = arrayListOf(Food(1, "Tasty 1", listOf()), Food(2, "Tasty 2", listOf()), Food(3, "Tasty 3", listOf()))
    }

}