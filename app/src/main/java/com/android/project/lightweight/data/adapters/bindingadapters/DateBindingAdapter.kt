package com.android.project.lightweight.data.adapters.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("currentDate")
fun TextView.setCurrentDate(pickedDate: String?) {
    pickedDate.let {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US) // ex.: 2020-09-03
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US) // ex.: Sep 03, 2020
        val outputDateStr = parseDate(pickedDate, inputFormat, outputFormat)
        text = outputDateStr
    }
}

private fun parseDate(inputDateString: String?, inputDateFormat: SimpleDateFormat, outputDateFormat: SimpleDateFormat): String {
    return if (inputDateString == null) {
        DateFormat.getDateInstance().format(Calendar.getInstance().time)
    } else {
        val date = inputDateFormat.parse(inputDateString)
        outputDateFormat.format(date)
    }
}