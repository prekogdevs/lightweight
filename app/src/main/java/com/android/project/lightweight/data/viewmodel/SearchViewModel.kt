package com.android.project.lightweight.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.project.lightweight.api.retrofit.model.Food
import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.repository.AbstractSearchRepository
import com.android.project.lightweight.persistence.transformer.EntityTransformer
import com.android.project.lightweight.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: AbstractSearchRepository) : ViewModel() {
    private val _foodResponse = MutableLiveData<Event<Resource<FoodResponse>>>()
    val foodResponse: LiveData<Event<Resource<FoodResponse>>> = _foodResponse

    fun searchForFood(query: String) {
        _foodResponse.value = Event(Resource.loading(null, query))
        viewModelScope.launch {
            val result = searchRepository.searchForFood(AppConstants.API_KEY, query)
            _foodResponse.value = Event(result)
        }
    }

    fun createDiaryEntryFromFood(food: Food): DiaryEntry {
        val requiredNutrients = filterNotRequiredNutrients(food)
        val consumedOn = DateFormatter.parseDateToLong(CurrentDate.currentDate)
        val nutrientEntries = EntityTransformer.transformFoodNutrientsToNutrientEntries(consumedOn, requiredNutrients)
        val diaryEntry = DiaryEntry(food.fdcId, food.description, consumedOn, 0, 0.0)
        diaryEntry.nutrientEntries = nutrientEntries
        return diaryEntry
    }

    private fun filterNotRequiredNutrients(food: Food) = food.foodNutrients.filter { AppConstants.all.contains(it.nutrientNumber.toInt()) }
}