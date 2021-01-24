package com.android.project.lightweight.ui

import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.android.project.lightweight.R
import net.cachapa.expandablelayout.ExpandableLayout


inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

fun ExpandableLayout.handleExpansion(headerTextView: TextView) {
    if (isExpanded) {
        headerTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow, 0)
        collapse()
    } else {
        headerTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0)
        expand()
    }
}