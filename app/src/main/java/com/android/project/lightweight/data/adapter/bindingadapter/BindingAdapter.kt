package com.android.project.lightweight.data.adapter.bindingadapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.android.project.lightweight.R
import com.android.project.lightweight.api.retrofit.model.FoodResponse
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.util.DateFormatter
import com.android.project.lightweight.util.Event
import com.android.project.lightweight.util.Resource
import com.android.project.lightweight.util.Status


@BindingAdapter("currentDate")
fun setCurrentDate(view: TextView, pickedDate: String?) {
    pickedDate.let {
        view.text = DateFormatter.parseDate(pickedDate)
    }
}

@BindingAdapter("resource")
fun setVisibility(root: ConstraintLayout, event: Event<Resource<FoodResponse>>?) {
    val content = event?.getContentIfNotHandled()
    content?.let { resource ->
        val status = resource.status
        status.let {
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

@BindingAdapter("detailsMenu")
fun setMenu(toolbar: Toolbar, diaryEntry: DiaryEntry?) {
    diaryEntry?.let { entry ->
        toolbar.title = entry.description
        if (entry.id == 0L) {
            toolbar.inflateMenu(R.menu.details_menu_save)
        } else {
            toolbar.inflateMenu(R.menu.details_menu_delete)
        }
    }
}