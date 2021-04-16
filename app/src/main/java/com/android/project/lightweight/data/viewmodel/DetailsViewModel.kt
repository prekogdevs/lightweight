package com.android.project.lightweight.data.viewmodel

import androidx.lifecycle.*
import com.android.project.lightweight.api.retrofit.model.Food
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.relations.DiaryEntryWithNutrients
import com.android.project.lightweight.persistence.repository.AbstractDiaryRepository
import com.android.project.lightweight.persistence.repository.AbstractNutrientRepository
import com.android.project.lightweight.persistence.transformer.EntityTransformer
import com.android.project.lightweight.util.AppConstants
import com.android.project.lightweight.util.CurrentDate
import com.android.project.lightweight.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val diaryRepository: AbstractDiaryRepository,
    private val nutrientRepository: AbstractNutrientRepository
) : ViewModel() {
    private val _foodie = MutableLiveData<Food>()
    val foodie: LiveData<Food> = _foodie
    private val diaryEntryId = MutableLiveData<Long>()

    val diaryEntryWithNutrients: LiveData<DiaryEntryWithNutrients> = Transformations.switchMap(diaryEntryId) { id ->
        id?.let {
            diaryRepository.getDiaryEntryWithNutrients(it)
        }
    }

    fun triggerInitialization(entry: DiaryEntry?, food: Food?) {
        entry?.let {
            diaryEntryId.value = it.id
        }
        food?.let {
            _foodie.value = it
        }
    }


    private fun createDiaryEntryFromFood(food: Food): DiaryEntry {
        val requiredNutrients = filterOutNotRequiredNutrients(food)
        val consumedOn = DateFormatter.parseDateToLong(CurrentDate.currentDate)
        val nutrientEntries = EntityTransformer.transformFoodNutrientsToNutrientEntries(consumedOn, requiredNutrients)
        val tmpDiaryEntry = DiaryEntry(food.fdcId, food.description, consumedOn, 0, 0.0)
        tmpDiaryEntry.nutrientEntries = nutrientEntries
        return tmpDiaryEntry
    }

    private fun filterOutNotRequiredNutrients(food: Food) =
        food.foodNutrients.filter { AppConstants.all.contains(it.nutrientNumber.toInt()) }

    //    fun setConsumptionDetails(consumptionAmount: Int) {
//        diaryEntry.consumedAmount = consumptionAmount
//        diaryEntry.consumedCalories = filter(diaryEntry.nutrientEntries, listOf(AppConstants.energyNutrientNumber)).first().consumedAmount
//    }
//
//    fun updateNutrients(amountValue: Int) {
//        val consumedNutrients = mutableListOf<NutrientEntry>()
//        for (nutrient in diaryEntry.nutrientEntries) {
//            nutrient.consumedAmount = (nutrient.originalComponentValueInPortion * amountValue) / 100
//            consumedNutrients.add(nutrient)
//        }
//        diaryEntry.nutrientEntries = consumedNutrients
//    }
//
    fun deleteDiaryEntry() = viewModelScope.launch {
        diaryEntryWithNutrients.value?.let { diaryEntryWithNutrients ->
            diaryRepository.deleteDiaryEntry(diaryEntryWithNutrients.diaryEntry.id)
        }
    }

    fun saveDiaryEntry() = viewModelScope.launch {
        val diaryEntry = createDiaryEntryFromFood(_foodie.value!!)
        val insertedDiaryEntryId = diaryRepository.insertDiaryEntry(diaryEntry)
        diaryEntry.nutrientEntries.map { it.diaryEntryId = insertedDiaryEntryId }
        nutrientRepository.insertNutrientEntries(diaryEntry.nutrientEntries)
    }

    fun filter(nutrientEntries: List<NutrientEntry>, filterList: List<Int>) =
        nutrientEntries.filter { nutrientEntry -> filterList.contains(nutrientEntry.nutrientNumber.toInt()) }
}
