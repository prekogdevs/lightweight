package com.android.project.lightweight.utilities

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class AppConstants {
    companion object {
        val TODAY_FORMATTED = DateFormatter.today()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        const val BASE_URL = "https://api.nal.usda.gov/fdc/v1/foods/"
    }
}