package com.android.project.lightweight.data.adapter.bindingadapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.android.project.lightweight.R
import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.util.DateFormatter
import com.android.project.lightweight.util.Status


@BindingAdapter("currentDate")
fun setCurrentDate(view: TextView, pickedDate: String?) {
    pickedDate.let {
        view.text = DateFormatter.parseDate(pickedDate)
    }
}

@BindingAdapter("status", "data", "query")
fun setVisibility(root: ConstraintLayout, status : Status?, data : FoodResponse?, query : String?) {
    status?.let {
        if (status == Status.SUCCESS) {
            val foods = data?.foods
            foods?.let {
                if (foods.isEmpty()) {
                    root.visibility = View.VISIBLE
                    val textView = root.findViewById<TextView>(R.id.empty_result_text)
                    textView.text = root.context.getString(R.string.no_result_with_given_query, query)
                } else {
                    root.visibility = View.GONE
                }
            }
        }
    }
}

@BindingAdapter("visibility")
fun setVisibility(view: ProgressBar, status: Status?) {
    status?.let {
        if (status == Status.LOADING) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}