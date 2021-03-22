package com.android.project.lightweight.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.repository.DiaryRepository
import com.android.project.lightweight.persistence.repository.NutrientRepository
import com.android.project.lightweight.utilities.AppConstants.carbsNutrientNumber
import com.android.project.lightweight.utilities.AppConstants.energyNutrientNumber
import com.android.project.lightweight.utilities.AppConstants.fatsNutrientNumber
import com.android.project.lightweight.utilities.AppConstants.proteinNutrientNumber
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val diaryRepository: DiaryRepository
    private val nutrientRepository: NutrientRepository
    private val consumedOn = MutableLiveData(CurrentDate.currentDate) // on start the Date will be "TODAY"
    val consumedFoods: LiveData<List<DiaryEntry>>
    val consumedEnergy: LiveData<Double>
    val consumedProtein: LiveData<Double>
    val consumedCarbs: LiveData<Double>
    val consumedFats: LiveData<Double>

    init {
        val diaryDao = DiaryDatabase(application).diaryDao()
        val nutrientDao = DiaryDatabase(application).nutrientDao()
        diaryRepository = DiaryRepository(diaryDao)
        nutrientRepository = NutrientRepository(nutrientDao)
        consumedFoods = Transformations.switchMap(consumedOn) { consumedOn ->
            diaryRepository.getEntries(DateFormatter.parseDateToLong(consumedOn))
        }
        consumedEnergy = Transformations.switchMap(consumedOn) { consumedOn ->
            nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), energyNutrientNumber)
        }
        consumedProtein = Transformations.switchMap(consumedOn) { consumedOn ->
            nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), proteinNutrientNumber)
        }
        consumedCarbs = Transformations.switchMap(consumedOn) { consumedOn ->
            nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), carbsNutrientNumber)
        }
        consumedFats = Transformations.switchMap(consumedOn) { consumedOn ->
            nutrientRepository.sumConsumedAmountByNutrients(DateFormatter.parseDateToLong(consumedOn), fatsNutrientNumber)
        }
    }

    fun changeDate(date: String) {
        consumedOn.value = date
    }
}