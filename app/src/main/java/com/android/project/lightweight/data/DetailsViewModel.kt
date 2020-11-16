package com.android.project.lightweight.data

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.android.project.lightweight.R
import com.android.project.lightweight.api.model.Food
import com.android.project.lightweight.api.model.FoodNutrient
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.persistence.repository.DiaryRepository
import com.android.project.lightweight.persistence.repository.NutrientRepository
import com.android.project.lightweight.persistence.transformer.EntityTransformer
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val general = listOf(203, 204, 605, 606, 645, 646, 205, 208, 291)
    private val vitamins = listOf(318, 323, 328, 430, 404, 405, 406, 410, 415, 418, 578, 431, 401, 421, 430)
    private val minerals = listOf(301, 312, 303, 304, 315, 305, 306, 317, 307, 309)

    private val diaryRepository: DiaryRepository
    private val nutrientRepository: NutrientRepository

    var diaryEntryId = MutableLiveData<Long>()
    var food = MutableLiveData<Food>()
    val nutrients = Transformations.switchMap(diaryEntryId) {
        DiaryDatabase(application).nutrientDao().getNutrientsByDiaryEntryId(diaryEntryId.value!!)
    }

    init {
        val diaryDao = DiaryDatabase(application).diaryDao()
        diaryRepository = DiaryRepository(diaryDao)
        val nutrientDao = DiaryDatabase(application).nutrientDao()
        nutrientRepository = NutrientRepository(nutrientDao)
    }

    fun setFood(food : Food) {
        this.food.value = food
    }

    fun setDiaryEntryId(diaryEntryId: Long) {
        this.diaryEntryId.value = diaryEntryId
    }

    private suspend fun insertDiaryEntry(entry: DiaryEntry) = diaryRepository.insertDiaryEntry(entry)

    private suspend fun insertNutrientEntries(nutrientEntries: List<NutrientEntry>) = nutrientRepository.insertNutrientEntries(nutrientEntries)

    fun insertDiaryEntryWithNutrientEntries(entry: DiaryEntry, food: List<FoodNutrient>) = viewModelScope.launch {
        val diaryEntryId = insertDiaryEntry(entry)
        val nutrientEntries = EntityTransformer.transformFoodNutrientsToNutrientEntries(food, diaryEntryId)
        insertNutrientEntries(nutrientEntries)
    }

    fun deleteDiaryEntry(foodId: Long, consumedOn: Long) = viewModelScope.launch {
        diaryRepository.deleteDiaryEntry(foodId, consumedOn)
    }

    fun filterFoodNutrients(view: View, foodNutrients: List<FoodNutrient>): List<FoodNutrient> {
        return when (view.id) {
            R.id.chip_general -> filter(foodNutrients, general)
            R.id.chip_vitamins -> filter(foodNutrients, vitamins)
            R.id.chip_minerals -> filter(foodNutrients, minerals)
            else -> {
                foodNutrients.filter { it.amount > 0 }
            }
        }
    }

    private fun filter(foodNutrients: List<FoodNutrient>, filterList: List<Int>): List<FoodNutrient> {
        return foodNutrients.filter { foodNutrient -> filterList.contains(foodNutrient.nutrientNumber.toInt()) && foodNutrient.amount > 0 }
    }
}