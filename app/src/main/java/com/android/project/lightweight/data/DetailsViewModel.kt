package com.android.project.lightweight.data

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.project.lightweight.R
import com.android.project.lightweight.network.Food
import com.android.project.lightweight.network.FoodNutrient
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.DiaryRepository

class DetailsViewModel(application: Application, val food: Food, val consumptionDate : Long) : AndroidViewModel(application) {
    private val general = listOf(203, 204, 605, 606, 645, 646, 205, 208, 291)
    private val vitamins = listOf(318, 323, 328, 430, 404, 405, 406, 410, 415, 418, 578, 431, 401, 421, 430)
    private val minerals = listOf(301, 312, 303, 304, 315, 305, 306, 317, 307, 309)

    private val diaryRepository: DiaryRepository

    init {
        val diaryDao = DiaryDatabase(application, viewModelScope).diaryDao()
        diaryRepository = DiaryRepository(diaryDao)
    }

    fun filterNutrients(view: View): List<FoodNutrient> {
        return when (view.id) {
            R.id.chip_general -> filter(food.foodNutrients, general)
            R.id.chip_vitamins -> filter(food.foodNutrients, vitamins)
            R.id.chip_minerals -> filter(food.foodNutrients, minerals)
            else -> {
                food.foodNutrients
            }
        }
    }

    private fun filter(foodNutrients: List<FoodNutrient>, filterList: List<Int>): List<FoodNutrient> {
        return foodNutrients.filter { filterList.contains(it.nutrientNumber) }
    }
}