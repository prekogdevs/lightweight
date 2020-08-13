package com.android.project.lightweight.utilities

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object UIUtils {
    fun closeKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}