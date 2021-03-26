package com.android.project.lightweight.utilities

import com.android.project.lightweight.BuildConfig

object AppConstants {
    const val DIARY_DATABASE_NAME = "diary_database"
    const val BASE_URL = "https://api.nal.usda.gov/fdc/v1/"
    const val API_KEY = BuildConfig.ApiKey
    const val energyNutrientNumber = 208
    const val proteinNutrientNumber = 203
    const val carbsNutrientNumber = 205
    const val fatsNutrientNumber = 204
    val general = listOf(proteinNutrientNumber, fatsNutrientNumber, 605, 606, 645, 646, carbsNutrientNumber, energyNutrientNumber, 291)
    val vitamins = listOf(318, 323, 328, 430, 404, 405, 406, 410, 415, 418, 578, 431, 401, 421, 430)
    val minerals = listOf(301, 312, 303, 304, 315, 305, 306, 317, 307, 309)
    val all = general.union(vitamins).union(minerals)
}