package com.android.project.lightweight.data.viewmodel

import androidx.lifecycle.*
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.AbstractDiaryRepository
import com.android.project.lightweight.persistence.repository.AbstractNutrientRepository
import com.android.project.lightweight.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val diaryRepository: AbstractDiaryRepository,
    private val nutrientRepository: AbstractNutrientRepository
) : ViewModel() {
    private var diaryEntryId = MutableLiveData<Long>()
    val nutrients: LiveData<List<NutrientEntry>> = Transformations.switchMap(diaryEntryId) {
        nutrientRepository.getNutrientEntriesByDiaryEntryId(it!!)
    }

    fun getNutrientEntriesByDiaryEntryId(id: Long) = diaryEntryId.postValue(id)

    fun setConsumptionDetails(diaryEntry: DiaryEntry, consumptionAmount: Int) {
        diaryEntry.consumedAmount = consumptionAmount
        diaryEntry.consumedKCAL = filter(diaryEntry.nutrients, listOf(AppConstants.energyNutrientNumber)).first().consumedAmount
    }

    fun calculateConsumedNutrients(diaryEntry: DiaryEntry, amountValue: Int): List<NutrientEntry> {
        val consumedNutrients = mutableListOf<NutrientEntry>()
        for (nutrient in diaryEntry.nutrients) {
            nutrient.consumedAmount = (nutrient.originalComponentValueInPortion * amountValue) / 100
            consumedNutrients.add(nutrient)
        }
        return consumedNutrients
    }

    fun deleteDiaryEntry(diaryEntryId: Long) = viewModelScope.launch {
        diaryRepository.deleteDiaryEntry(diaryEntryId)
    }

    fun saveDiaryEntry(diaryEntry: DiaryEntry) = viewModelScope.launch {
        val diaryEntryId = diaryRepository.insertDiaryEntry(diaryEntry)
        diaryEntry.nutrients.map { it.diaryEntryId = diaryEntryId }
        nutrientRepository.insertNutrientEntries(diaryEntry.nutrients)
    }

    fun filter(nutrientEntries: List<NutrientEntry>, filterList: List<Int>) =
        nutrientEntries.filter { nutrientEntry -> filterList.contains(nutrientEntry.nutrientNumber.toInt()) }
}