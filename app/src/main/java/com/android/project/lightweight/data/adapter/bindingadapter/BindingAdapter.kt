package com.android.project.lightweight.data.adapter.bindingadapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.android.project.lightweight.R
import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.util.DateFormatter
import com.android.project.lightweight.util.Resource
import com.android.project.lightweight.util.Status


@BindingAdapter("currentDate")
fun setCurrentDate(view: TextView, pickedDate: String?) {
    pickedDate.let {
        view.text = DateFormatter.parseDate(pickedDate)
    }
}

@BindingAdapter("visibility")
fun setVisibility(root: ConstraintLayout, resource: Resource<FoodResponse>?) {
    val status = resource?.status
    status?.let {
        if (status == Status.SUCCESS) {
            val foods = resource.data?.foods
            foods?.let {
                if (foods.isEmpty()) {
                    root.visibility = View.VISIBLE
                    val textView = root.findViewById<TextView>(R.id.empty_result_text)
                    textView.text = root.context.getString(R.string.no_result_with_given_query, resource.query)
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