package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import kotlinx.coroutines.Deferred

interface AbstractSearchRepository {
    suspend fun searchForFood(apiKey: String, query: String): Deferred<FoodResponse>
}