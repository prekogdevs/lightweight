package com.android.project.lightweight.utilities

import com.android.project.lightweight.BuildConfig

class AppConstants {
    companion object {
        const val BASE_URL = "https://api.nal.usda.gov/fdc/v1/foods/"
        const val API_KEY = BuildConfig.ApiKey
    }
}