package com.android.project.lightweight.data.adapter.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.project.lightweight.util.DateFormatter


@BindingAdapter("currentDate")
fun TextView.setCurrentDate(pickedDate: String?) {
    pickedDate.let {
        text = DateFormatter.parseDate(pickedDate)
    }
}