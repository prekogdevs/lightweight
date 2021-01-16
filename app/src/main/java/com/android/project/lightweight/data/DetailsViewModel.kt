package com.android.project.lightweight.data

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.android.project.lightweight.R
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.DiaryRepository
import com.android.project.lightweight.persistence.repository.NutrientRepository
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val kcal = 208
    private val general = listOf(203, 204, 605, 606, 645, 646, 205, kcal, 291)
    private val vitamins = listOf(318, 323, 328, 430, 404, 405, 406, 410, 415, 418, 578, 431, 401, 421, 430)
    private val minerals = listOf(301, 312, 303, 304, 315, 305, 306, 317, 307, 309)

    private val diaryRepository: DiaryRepository
    private val nutrientRepository: NutrientRepository

    private var diaryEntryId = MutableLiveData<Long>()
    val nutrients = Transformations.switchMap(diaryEntryId) {
        DiaryDatabase(application).nutrientDao().getNutrientEntriesByDiaryEntryId(diaryEntryId.value!!)
    }

    fun getNutrientEntriesByDiaryEntryId(id: Long) {
        diaryEntryId.value = id
    }

    init {
        val diaryDao = DiaryDatabase(application).diaryDao()
        diaryRepository = DiaryRepository(diaryDao)
        val nutrientDao = DiaryDatabase(application).nutrientDao()
        nutrientRepository = NutrientRepository(nutrientDao)
    }


    private suspend fun insertDiaryEntry(entry: DiaryEntry) = diaryRepository.insertDiaryEntry(entry)

    private suspend fun insertNutrientEntriesToEntry(nutrientEntries: List<NutrientEntry>) {
        nutrientRepository.insertNutrientEntries(nutrientEntries)
    }

    fun insertDiaryEntryWithNutrientEntries(entry: DiaryEntry) = viewModelScope.launch {
        val diaryEntryId = insertDiaryEntry(entry)
        entry.nutrients.map { it.diaryEntryId = diaryEntryId }.toList() // If the entry is inserted, the foreign key must be updated to the new diary entry's id.
        insertNutrientEntriesToEntry(entry.nutrients)
    }

    fun deleteDiaryEntry(diaryEntryId: Long) = viewModelScope.launch {
        diaryRepository.deleteDiaryEntry(diaryEntryId)
    }

    fun filterFoodNutrients(view: View, nutrientEntries: List<NutrientEntry>): List<NutrientEntry> {
        return when (view.id) {
            R.id.chip_general -> filter(nutrientEntries, general)
            R.id.chip_vitamins -> filter(nutrientEntries, vitamins)
            R.id.chip_minerals -> filter(nutrientEntries, minerals)
            else -> {
                nutrientEntries
            }
        }
    }

    private fun filter(nutrientEntries: List<NutrientEntry>, filterList: List<Int>) =
        nutrientEntries.filter { nutrientEntry -> filterList.contains(nutrientEntry.nutrientNumber.toInt()) }


    fun energyInFood(nutrientEntries: List<NutrientEntry>) = filter(nutrientEntries, listOf(kcal)).first().consumedAmount.toInt()

    fun calculateConsumedNutrients(diaryEntry: DiaryEntry, amountValue: Int): List<NutrientEntry> {
        val consumedNutrients = mutableListOf<NutrientEntry>()
        for (nutrient in diaryEntry.nutrients) {
            nutrient.consumedAmount = (nutrient.originalComponentValueInPortion * amountValue) / 100
            consumedNutrients.add(nutrient)
        }
        return consumedNutrients
    }
}