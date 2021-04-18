package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.util.Resource

class FakeSearchRepository : AbstractSearchRepository {
    private var shouldReturnNetworkError = true

    override suspend fun searchForFood(apiKey: String, query: String): Resource<FoodResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null, query)
        } else {
            Resource.success(FoodResponse(listOf()), query)
        }
    }
}