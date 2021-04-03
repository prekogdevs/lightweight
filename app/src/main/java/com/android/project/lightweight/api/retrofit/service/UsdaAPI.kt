package com.android.project.lightweight.api.retrofit.service

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsdaAPI {
    @GET("search")
    suspend fun searchForFood(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<FoodResponse>
}



