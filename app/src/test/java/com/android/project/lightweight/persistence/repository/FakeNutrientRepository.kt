package com.android.project.lightweight.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.project.lightweight.persistence.entity.NutrientEntry

class FakeNutrientRepository : AbstractNutrientRepository {

    private val entries = mutableListOf<NutrientEntry>()
    private val observableNutrientEntriesByDiaryEntryId = MutableLiveData<List<NutrientEntry>>()
    private val observableConsumedAmount = MutableLiveData<Double>()

    override suspend fun insertNutrientEntries(nutrientEntries: List<NutrientEntry>) {
        entries.addAll(nutrientEntries)
    }

    override fun getNutrientEntriesByDiaryEntryId(diaryEntryId: Long): LiveData<List<NutrientEntry>> {
        observableNutrientEntriesByDiaryEntryId.postValue(entries.filter { it.diaryEntryId == diaryEntryId }.toMutableList())
        return observableNutrientEntriesByDiaryEntryId
    }

    override fun sumConsumedAmountByNutrients(consumedOn: Long, nutrientNumber: Int): LiveData<Double> {
        observableConsumedAmount.postValue(entries.filter { it.consumedOn == consumedOn && it.nutrientNumber == nutrientNumber.toDouble() }.sumByDouble { it.consumedAmount })
        return observableConsumedAmount
    }
}