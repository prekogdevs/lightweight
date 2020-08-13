package com.android.project.lightweight.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://api.nal.usda.gov/fdc/v1/foods/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit =
    Retrofit
        .Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

interface FoodApiService {
    @GET("search")
    fun getFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Deferred<FoodProperty>
}

object FoodApi {
    val retrofitService: FoodApiService by lazy {
        retrofit.create(FoodApiService::class.java)
    }
}



