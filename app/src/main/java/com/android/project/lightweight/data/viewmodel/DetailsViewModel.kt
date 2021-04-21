package com.android.project.lightweight.data.viewmodel

import androidx.lifecycle.*
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.AbstractDiaryRepository
import com.android.project.lightweight.persistence.repository.AbstractNutrientRepository
import com.android.project.lightweight.util.AppConstants
import com.android.project.lightweight.util.AppConstants.energyNutrientNumber
import com.android.project.lightweight.util.FilterCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val diaryRepository: AbstractDiaryRepository,
    private val nutrientRepository: AbstractNutrientRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val diaryEntryId = MutableLiveData<Long>()
    val diaryEntry: DiaryEntry = savedStateHandle.get<DiaryEntry>("diaryEntry")!!

    // DiaryFragment -> DetailsFragment (when DiaryEntry exists in DB)
    val nutrients: LiveData<List<NutrientEntry>> = Transformations.switchMap(diaryEntryId) {
        it?.let {
            nutrientRepository.getNutrientEntriesByDiaryEntryId(it)
        }
    }

    init {
        if (diaryEntry.id != 0L) {
            diaryEntryId.postValue(diaryEntry.id)
        }
    }

    fun filterByNutrientCategory(filterCategory: FilterCategory): List<NutrientEntry> {
        val nutrientsToFilter = nutrients.value ?: diaryEntry.nutrientEntries
        return when (filterCategory) {
            FilterCategory.ALL -> nutrientsToFilter
            FilterCategory.GENERAL -> nutrientsToFilter.filter { AppConstants.general.contains(it.nutrientNumber.toInt()) }
            FilterCategory.VITAMINS -> nutrientsToFilter.filter { AppConstants.vitamins.contains(it.nutrientNumber.toInt()) }
            FilterCategory.MINERALS -> nutrientsToFilter.filter { AppConstants.minerals.contains(it.nutrientNumber.toInt()) }
        }
    }

    fun updateDiaryEntry(consumptionAmount: Int) {
        diaryEntry.consumedAmount = consumptionAmount
        diaryEntry.nutrientEntries.forEach { nutrient ->
            nutrient.consumedAmount = (nutrient.originalComponentValueInPortion * consumptionAmount) / 100
        }
        diaryEntry.consumedCalories = diaryEntry.nutrientEntries.first { it.nutrientNumber.toInt() == energyNutrientNumber }.consumedAmount
    }

    fun deleteDiaryEntry() = viewModelScope.launch {
        diaryRepository.deleteDiaryEntry(diaryEntry.id)
    }

    fun saveDiaryEntry() = viewModelScope.launch {
        val insertedDiaryEntryId = diaryRepository.insertDiaryEntry(diaryEntry)
        diaryEntry.nutrientEntries.map { it.diaryEntryId = insertedDiaryEntryId }
        nutrientRepository.insertNutrientEntries(diaryEntry.nutrientEntries)
    }
}