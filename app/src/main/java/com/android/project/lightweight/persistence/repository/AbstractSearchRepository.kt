package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import retrofit2.Response

interface AbstractSearchRepository {
    suspend fun searchForFood(apiKey: String, query: String): Response<FoodResponse>
}