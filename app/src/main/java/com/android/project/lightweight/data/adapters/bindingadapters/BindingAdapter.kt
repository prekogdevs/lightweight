package com.android.project.lightweight.data.adapters.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.project.lightweight.utilities.DateFormatter


@BindingAdapter("currentDate")
fun TextView.setCurrentDate(pickedDate: String?) {
    pickedDate.let {
        text = DateFormatter.parseDate(pickedDate)
    }
}