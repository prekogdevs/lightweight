package com.android.project.lightweight.utilities

import com.android.project.lightweight.BuildConfig

object AppConstants {
    const val BASE_URL = "https://api.nal.usda.gov/fdc/v1/"
    const val API_KEY = BuildConfig.ApiKey
    private const val protein = 203
    private const val carbs = 205
    private const val fats = 204
    private const val kcal = 208
    val general = listOf(protein, fats, 605, 606, 645, 646, carbs, kcal, 291)
    val vitamins = listOf(318, 323, 328, 430, 404, 405, 406, 410, 415, 418, 578, 431, 401, 421, 430)
    val minerals = listOf(301, 312, 303, 304, 315, 305, 306, 317, 307, 309)
    val all = general.union(vitamins).union(minerals)
}