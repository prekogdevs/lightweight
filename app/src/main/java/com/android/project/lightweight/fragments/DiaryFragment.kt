package com.android.project.lightweight.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.project.lightweight.R
import com.android.project.lightweight.databinding.FragmentDiaryBinding
import com.android.project.lightweight.utilities.UIUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

class DiaryFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentDiaryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        binding.lifecycleOwner = this
        binding.btnPickDate.setOnClickListener {
            val dialog = UIUtils.createDatePickerDialog(requireContext(), this)
            dialog.show(parentFragmentManager, "Datepickerdialog")
        }
        return binding.root
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // The picked date in the given format will be handled by [DateBindingAdapter]
        binding.pickedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
    }
}