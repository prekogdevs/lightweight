package com.android.project.lightweight.data

import android.app.Application
import androidx.lifecycle.*
import com.android.project.lightweight.network.Food
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.DiaryRepository

class DiaryViewModel(application: Application, consumptionDate: String) : AndroidViewModel(application) {
    private val _consumedFoods = MutableLiveData<ArrayList<Food>>()
    val consumedFoods: LiveData<ArrayList<Food>>
        get() = _consumedFoods

    private val diaryRepository: DiaryRepository

    init {
        val diaryDao = DiaryDatabase(application).diaryDao()
        diaryRepository = DiaryRepository(diaryDao, consumptionDate)
    }

}