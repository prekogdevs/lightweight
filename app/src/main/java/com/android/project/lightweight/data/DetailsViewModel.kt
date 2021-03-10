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
    private val protein = 203
    private val carbs = 205
    private val fats = 204
    private val kcal = 208
    private val general = listOf(protein, fats, 605, 606, 645, 646, carbs, kcal, 291)
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

    fun insertDiaryEntryWithNutrientEntries(diaryEntry: DiaryEntry) = viewModelScope.launch {
        diaryEntry.unitName = "g"
        diaryEntry.kcal = energyInFood(diaryEntry.nutrients)
        diaryEntry.protein = proteinInFood(diaryEntry.nutrients)
        diaryEntry.carbs = carbsInFood(diaryEntry.nutrients)
        diaryEntry.fats = fatsInFood(diaryEntry.nutrients)
        val diaryEntryId = insertDiaryEntry(diaryEntry)
        diaryEntry.nutrients.map { it.diaryEntryId = diaryEntryId }.toList() // If the entry is inserted, the foreign key must be updated to the new diary entry's id.
        insertNutrientEntriesToEntry(diaryEntry.nutrients)
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


    private fun givenNutrientInFood(nutrientEntries: List<NutrientEntry>, nutrientCode: Int) = filter(nutrientEntries, listOf(nutrientCode)).first().consumedAmount.toInt()
    private fun energyInFood(nutrientEntries: List<NutrientEntry>) = givenNutrientInFood(nutrientEntries, kcal)
    private fun proteinInFood(nutrientEntries: List<NutrientEntry>) = givenNutrientInFood(nutrientEntries, protein)
    private fun carbsInFood(nutrientEntries: List<NutrientEntry>) = givenNutrientInFood(nutrientEntries, carbs)
    private fun fatsInFood(nutrientEntries: List<NutrientEntry>) = givenNutrientInFood(nutrientEntries, fats)

    fun calculateConsumedNutrients(diaryEntry: DiaryEntry, amountValue: Int): List<NutrientEntry> {
        val consumedNutrients = mutableListOf<NutrientEntry>()
        for (nutrient in diaryEntry.nutrients) {
            nutrient.consumedAmount = (nutrient.originalComponentValueInPortion * amountValue) / 100
            consumedNutrients.add(nutrient)
        }
        return consumedNutrients
    }
}