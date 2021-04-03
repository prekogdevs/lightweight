package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.api.retrofit.service.UsdaAPI
import com.android.project.lightweight.util.AppConstants
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class SearchRepository @Inject constructor(private val usdaAPI: UsdaAPI) : AbstractSearchRepository {
    override suspend fun searchForFood(apiKey: String, query: String): Deferred<FoodResponse> {
        return usdaAPI.searchForFood(AppConstants.API_KEY, query)
    }
}