package com.android.project.lightweight.utilities

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateFormatter {
    private val defaultInputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US) // ex.: 2020-09-03
    private val defaultOutputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US) // ex.: Sep 03, 2020

    fun today(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return current.format(formatter)
    }

    fun formatToValidDate(year: Int, monthOfYear: Int, dayOfMonth: Int) = "$year-${String.format("%02d", monthOfYear)}-${String.format("%02d", dayOfMonth)}"

    fun parseDateToLong(date: String) = date.replace("-", "").toLong()


    // Format dates from yyyy-MM-dd to yyyyMMdd
    // e.g.: 2020-10-18 will be 20201018
    fun parseDate(inputDateString: String?, inputDateFormat: SimpleDateFormat = defaultInputFormat, outputDateFormat: SimpleDateFormat = defaultOutputFormat): String {
        return if (inputDateString == null) {
            DateFormat.getDateInstance().format(Calendar.getInstance().time)
        } else {
            val date = inputDateFormat.parse(inputDateString)
            outputDateFormat.format(date)
        }
    }
}