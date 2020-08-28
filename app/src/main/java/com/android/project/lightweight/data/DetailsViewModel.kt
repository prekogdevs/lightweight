package com.android.project.lightweight.data

import androidx.lifecycle.ViewModel
import com.android.project.lightweight.network.FoodNutrient

class DetailsViewModel : ViewModel() {

    // TODO: Extract these constants later (the numbers are nutrient codes from USDA API)
    private val general = listOf(203, 204, 605, 606, 645, 646, 205, 208, 291)
    private val vitamins = listOf(318, 323, 328, 430, 404, 405, 406, 410, 415, 418, 578, 431, 401, 421, 430)
    private val minerals = listOf(301, 312, 303, 304, 315, 305, 306, 317, 307, 309)

    // TODO: Click event in DetailsFragment will send a filter value to the DetailsViewModel
     //Based on this value this function will return a subset of food nutrients (general/vitamins or minerals)
    fun filterNutrients(foodNutrients: List<FoodNutrient>, filterList: List<Int>): List<FoodNutrient> {
        return foodNutrients.filter { filterList.contains(it.nutrientNumber) }
    }
}