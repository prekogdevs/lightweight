package com.android.project.lightweight.data.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.DiaryRepository
import com.android.project.lightweight.persistence.repository.NutrientRepository
import com.android.project.lightweight.util.AppConstants.energyNutrientNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val nutrientRepository: NutrientRepository,
    application: Application
) : AndroidViewModel(application) {
    private var diaryEntryId = MutableLiveData<Long>()
    val nutrients: LiveData<List<NutrientEntry>> = Transformations.switchMap(diaryEntryId) {
        nutrientRepository.getNutrientEntriesByDiaryEntryId(it!!)
    }

    fun getNutrientEntriesByDiaryEntryId(id: Long) {
        diaryEntryId.postValue(id)
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
        diaryEntry.consumedKCAL = energyInFood(diaryEntry.nutrients)
        val diaryEntryId = insertDiaryEntry(diaryEntry)
        // If the entry is inserted, the foreign key must be updated to the new diary entry's id.
        diaryEntry.nutrients.map { it.diaryEntryId = diaryEntryId }.toList()
        insertNutrientEntriesToEntry(diaryEntry.nutrients)
    }

    fun filter(nutrientEntries: List<NutrientEntry>, filterList: List<Int>) =
        nutrientEntries.filter { nutrientEntry -> filterList.contains(nutrientEntry.nutrientNumber.toInt()) }

    private fun energyInFood(nutrientEntries: List<NutrientEntry>) = filter(nutrientEntries, listOf(energyNutrientNumber)).first().consumedAmount

    private suspend fun insertDiaryEntry(entry: DiaryEntry) = diaryRepository.insertDiaryEntry(entry)

    private suspend fun insertNutrientEntriesToEntry(nutrientEntries: List<NutrientEntry>) =
        nutrientRepository.insertNutrientEntries(nutrientEntries)

}