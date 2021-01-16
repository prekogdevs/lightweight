package com.android.project.lightweight.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val consumedOn = MutableLiveData(CurrentDate.currentDate) // on start the Date will be "TODAY"
    val consumedFoods = Transformations.switchMap(consumedOn) { consumedOn ->
        DiaryDatabase(application).diaryDao().getEntries(DateFormatter.parseDateToLong(consumedOn))
    }

    fun changeDate(date : String) {
        consumedOn.value = date
    }
}