package com.android.project.lightweight.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.repository.AbstractDiaryRepository
import com.android.project.lightweight.persistence.repository.AbstractNutrientRepository
import com.android.project.lightweight.util.AppConstants.carbsNutrientNumber
import com.android.project.lightweight.util.AppConstants.energyNutrientNumber
import com.android.project.lightweight.util.AppConstants.fatsNutrientNumber
import com.android.project.lightweight.util.AppConstants.proteinNutrientNumber
import com.android.project.lightweight.util.CurrentDate
import com.android.project.lightweight.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: AbstractDiaryRepository,
    private val nutrientRepository: AbstractNutrientRepository
) : ViewModel() {
    private val consumedOn = MutableLiveData(CurrentDate.currentDate) // on start the Date will be "TODAY"
    val consumedFoods: LiveData<List<DiaryEntry>> = Transformations.switchMap(consumedOn) { consumedOn ->
        diaryRepository.getEntries(DateFormatter.parseDateToLong(consumedOn))
    }
    val consumedEnergy: LiveData<Double> = Transformations.switchMap(consumedOn) { consumedOn ->
        nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), energyNutrientNumber)
    }
    val consumedProtein: LiveData<Double> = Transformations.switchMap(consumedOn) { consumedOn ->
        nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), proteinNutrientNumber)
    }
    val consumedCarbs: LiveData<Double> = Transformations.switchMap(consumedOn) { consumedOn ->
        nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), carbsNutrientNumber)
    }
    val consumedFats: LiveData<Double> = Transformations.switchMap(consumedOn) { consumedOn ->
        nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), fatsNutrientNumber)
    }

    fun changeDate(date: String) = consumedOn.postValue(date)
}