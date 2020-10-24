package com.android.project.lightweight.data

import android.app.Application
import androidx.lifecycle.*
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    val consumedWhen = MutableLiveData(CurrentDate.currentDate) // on start the Date will be "TODAY"
    val consumedFoods = Transformations.switchMap(consumedWhen) { consumedWhen ->
        DiaryDatabase(application).diaryDao().getEntries(DateFormatter.parseDateToLong(consumedWhen))
    }

    fun changeDate(date : String) {
        consumedWhen.value = date
    }
}