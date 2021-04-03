package com.android.project.lightweight.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.project.lightweight.api.retrofit.model.Food
import com.android.project.lightweight.persistence.repository.AbstractSearchRepository
import com.android.project.lightweight.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: AbstractSearchRepository) : ViewModel() {

    private val _response = MutableLiveData<List<Food>>()
    val response: LiveData<List<Food>>
        get() = _response

    fun searchForFood(query: String) {
        viewModelScope.launch {
            val getFoodsDeferred = searchRepository.searchForFood(AppConstants.API_KEY, query)
            try {
                val result = getFoodsDeferred.await()
                _response.value = result.foods
            } catch (e: Exception) {
                _response.value = listOf()
            }
        }
    }

    fun filterOutNotRequiredNutrients(food: Food) =
        food.foodNutrients.filter { AppConstants.all.contains(it.nutrientNumber.toInt()) }
}