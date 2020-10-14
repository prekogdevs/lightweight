package com.android.project.lightweight.utilities

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TodayFormatter {
    fun today(): Long {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        return current.format(formatter).toLong()
    }
}