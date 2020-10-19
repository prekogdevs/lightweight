package com.android.project.lightweight.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.project.lightweight.persistence.entity.Food
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.DiaryRepository

class DiaryViewModel(application: Application, consumedWhen: Long) : AndroidViewModel(application) {
    val consumedFoods: LiveData<List<Food>>
    private val diaryRepository: DiaryRepository

    init {
        val diaryDao = DiaryDatabase(application).diaryDao()
        diaryRepository = DiaryRepository(diaryDao, consumedWhen)
        consumedFoods = diaryRepository.entries
    }

}