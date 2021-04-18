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
    private val diaryEntry = MutableLiveData<DiaryEntry>()
    val nutrients: LiveData<List<NutrientEntry>> = Transformations.switchMap(diaryEntry) {
        it?.let {entry ->
            nutrientRepository.getNutrientEntriesByDiaryEntryId(entry.id)
        }
    }

    fun setDiaryEntry(entry: DiaryEntry) = diaryEntry.postValue(entry)

    fun filter(nutrientEntries: List<NutrientEntry>, filterList: List<Int>) =
        nutrientEntries.filter { nutrientEntry -> filterList.contains(nutrientEntry.nutrientNumber.toInt()) }

    fun updateDiaryEntry(diaryEntry: DiaryEntry, consumptionAmount: Int) {
        diaryEntry.consumedAmount = consumptionAmount
        diaryEntry.nutrientEntries = calculateConsumedNutrients(diaryEntry.nutrientEntries, consumptionAmount)
        diaryEntry.consumedCalories = filter(diaryEntry.nutrientEntries, listOf(AppConstants.energyNutrientNumber)).first().consumedAmount
    }

    private fun calculateConsumedNutrients(nutrients: List<NutrientEntry>, consumptionAmount: Int): List<NutrientEntry> {
        val consumedNutrients = mutableListOf<NutrientEntry>()
        for (nutrient in nutrients) {
            nutrient.consumedAmount = (nutrient.originalComponentValueInPortion * consumptionAmount) / 100
            consumedNutrients.add(nutrient)
        }
        return consumedNutrients
    }

    fun deleteDiaryEntry(diaryEntryId: Long) = viewModelScope.launch {
        diaryRepository.deleteDiaryEntry(diaryEntryId)
    }

    fun saveDiaryEntry(diaryEntry: DiaryEntry) = viewModelScope.launch {
        val insertedDiaryEntryId = diaryRepository.insertDiaryEntry(diaryEntry)
        diaryEntry.nutrientEntries.map { it.diaryEntryId = insertedDiaryEntryId }
        nutrientRepository.insertNutrientEntries(diaryEntry.nutrientEntries)
    }
}