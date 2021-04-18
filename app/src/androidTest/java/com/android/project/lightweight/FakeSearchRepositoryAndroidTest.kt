package com.android.project.lightweight

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.persistence.repository.AbstractSearchRepository
import com.android.project.lightweight.util.Resource

class FakeSearchRepositoryAndroidTest : AbstractSearchRepository {
    private var shouldReturnNetworkError = true

    override suspend fun searchForFood(apiKey: String, query: String): Resource<FoodResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null, query)
        } else {
            Resource.success(FoodResponse(listOf()), query)
        }
    }
}