package com.android.project.lightweight.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.project.lightweight.api.model.Food
import com.android.project.lightweight.api.FoodApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _response = MutableLiveData<List<Food>>()
    val response: LiveData<List<Food>>
        get() = _response

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getFoods(apiKey : String, query : String) {
        coroutineScope.launch {
            val getFoodsDeferred = FoodApi.retrofitService.getFoods(apiKey, query)
            try {
                val result = getFoodsDeferred.await()
                _response.value = result.foods
            }
            catch (e : Exception) {
                _response.value = listOf()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}