package com.android.project.lightweight.data

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.android.project.lightweight.R
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.DiaryRepository
import com.android.project.lightweight.persistence.repository.NutrientRepository
import com.android.project.lightweight.utilities.AppConstants
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val diaryRepository: DiaryRepository
    private val nutrientRepository: NutrientRepository
    private var diaryEntryId = MutableLiveData<Long>()
    val nutrients: LiveData<List<NutrientEntry>>

    init {
        val diaryDao = DiaryDatabase(application).diaryDao()
        val nutrientDao = DiaryDatabase(application).nutrientDao()
        diaryRepository = DiaryRepository(diaryDao)
        nutrientRepository = NutrientRepository(nutrientDao)
        nutrients = Transformations.switchMap(diaryEntryId) {
            nutrientRepository.getNutrientEntriesByDiaryEntryId(diaryEntryId.value!!)
        }
    }

    fun getNutrientEntriesByDiaryEntryId(id: Long) {
        diaryEntryId.value = id
    }

    fun insertDiaryEntryWithNutrientEntries(diaryEntry: DiaryEntry) = viewModelScope.launch {
        val diaryEntryId = insertDiaryEntry(diaryEntry)
        diaryEntry.nutrients.map { it.diaryEntryId = diaryEntryId }.toList() // If the entry is inserted, the foreign key must be updated to the new diary entry's id.
        insertNutrientEntriesToEntry(diaryEntry.nutrients)
    }

    fun deleteDiaryEntry(diaryEntryId: Long) = viewModelScope.launch {
        diaryRepository.deleteDiaryEntry(diaryEntryId)
    }

    fun filterFoodNutrients(view: View, nutrientEntries: List<NutrientEntry>): List<NutrientEntry> {
        return when (view.id) {
            R.id.chip_general -> filter(nutrientEntries, AppConstants.general)
            R.id.chip_vitamins -> filter(nutrientEntries, AppConstants.vitamins)
            R.id.chip_minerals -> filter(nutrientEntries, AppConstants.minerals)
            else -> {
                nutrientEntries
            }
        }
    }

    fun calculateConsumedNutrients(diaryEntry: DiaryEntry, amountValue: Int): List<NutrientEntry> {
        val consumedNutrients = mutableListOf<NutrientEntry>()
        for (nutrient in diaryEntry.nutrients) {
            nutrient.consumedAmount = (nutrient.originalComponentValueInPortion * amountValue) / 100
            consumedNutrients.add(nutrient)
        }
        return consumedNutrients
    }

    private fun filter(nutrientEntries: List<NutrientEntry>, filterList: List<Int>) =
        nutrientEntries.filter { nutrientEntry -> filterList.contains(nutrientEntry.nutrientNumber.toInt()) }

    private suspend fun insertDiaryEntry(entry: DiaryEntry) = diaryRepository.insertDiaryEntry(entry)

    private suspend fun insertNutrientEntriesToEntry(nutrientEntries: List<NutrientEntry>) {
        nutrientRepository.insertNutrientEntries(nutrientEntries)
    }
}