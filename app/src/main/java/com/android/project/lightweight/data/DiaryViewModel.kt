package com.android.project.lightweight.data

import android.app.Application
import androidx.lifecycle.*
import com.android.project.lightweight.network.Food
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.DiaryRepository
import com.android.project.lightweight.persistence.entities.DiaryEntry

class DiaryViewModel(application: Application, consumptionDate: Long) : AndroidViewModel(application) {
    val consumedFoods: LiveData<List<DiaryEntry>>
    private val diaryRepository: DiaryRepository

    init {
        val diaryDao = DiaryDatabase(application, viewModelScope).diaryDao()
        diaryRepository = DiaryRepository(diaryDao, consumptionDate)
        consumedFoods = diaryRepository.entries
    }

}