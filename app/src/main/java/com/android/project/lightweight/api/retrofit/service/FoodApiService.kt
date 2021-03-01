package com.android.project.lightweight.api.retrofit.service

import com.android.project.lightweight.api.retrofit.model.FoodResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {
    @GET("search")
    fun getFoodsAsync(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Deferred<FoodResponse>
}



