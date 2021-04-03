package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.util.Resource

interface AbstractSearchRepository {
    suspend fun searchForFood(apiKey: String, query: String): Resource<FoodResponse>
}