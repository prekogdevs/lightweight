package com.android.project.lightweight.persistence.repository

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.api.retrofit.service.UsdaAPI
import com.android.project.lightweight.util.Resource
import javax.inject.Inject

class SearchRepository @Inject constructor(private val usdaAPI: UsdaAPI) : AbstractSearchRepository {
    override suspend fun searchForFood(apiKey: String, query: String): Resource<FoodResponse> {
        return try {
            val response = usdaAPI.searchForFood(apiKey, query)
            if (response.isSuccessful) {
                response.body()?.let { foodResponse ->
                    return@let Resource.success(foodResponse, query)
                } ?: Resource.error("Unknown error occured - response body is null", null, query)
            } else {
                Resource.error("An unknown error occured - response was not successful", null, query)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check you internet connection", null, query)
        }
    }
}