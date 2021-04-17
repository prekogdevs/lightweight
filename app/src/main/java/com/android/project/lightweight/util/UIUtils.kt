package com.android.project.lightweight.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.android.project.lightweight.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*

object UIUtils {
    fun closeKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun createDatePickerDialog(context: Context, listener: DatePickerDialog.OnDateSetListener): DatePickerDialog {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog.newInstance(listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dialog.accentColor = ContextCompat.getColor(context, R.color.mdtp_dark_gray)
        return dialog
    }

    fun createAnchoredSnackbar(activity: Activity, text: String, length : Int): Snackbar {
        val bottomNavigationView = activity.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val snack = Snackbar.make(bottomNavigationView, text, length)
        snack.anchorView = bottomNavigationView
        return snack
    }
}