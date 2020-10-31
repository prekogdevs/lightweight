package com.android.project.lightweight.data

import android.app.Application
import androidx.lifecycle.*
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    val consumedOn = MutableLiveData(CurrentDate.currentDate) // on start the Date will be "TODAY"
    val consumedFoods = Transformations.switchMap(consumedOn) { consumedOn ->
        DiaryDatabase(application).diaryDao().getEntries(DateFormatter.parseDateToLong(consumedOn))
    }

    fun changeDate(date : String) {
        consumedOn.value = date
    }
}