package com.android.project.lightweight.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.data.viewmodel.DiaryViewModel
import com.android.project.lightweight.data.adapters.DiaryEntryAdapter
import com.android.project.lightweight.data.adapters.OnDiaryEntryClickListener
import com.android.project.lightweight.databinding.FragmentDiaryBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.ui.extensions.handleExpansion
import com.android.project.lightweight.utilities.AppConstants.DATE_PICKER_DIALOG_TAG
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter
import com.android.project.lightweight.utilities.UIUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentDiaryBinding
    private val diaryEntryAdapter by lazy {
        DiaryEntryAdapter(object : OnDiaryEntryClickListener {
            override fun onClick(diaryEntry: DiaryEntry) {
                findNavController().navigate(DiaryFragmentDirections.actionDiaryFragmentToDetailsFragment(diaryEntry))
            }
        })
    }
    private val diaryViewModel: DiaryViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        binding.lifecycleOwner = this
        binding.pickedDate = CurrentDate.currentDate
        binding.diaryViewModel = diaryViewModel
        binding.diaryRecyclerview.apply {
            adapter = diaryEntryAdapter
            setHasFixedSize(true)
        }
        binding.consumedNutrientsSummaryIncluded.txtNutrientSummary.setOnClickListener {
            binding.consumedNutrientsSummaryIncluded.nutrientSummaryExpandableLayout.handleExpansion(it as TextView)
        }
        binding.btnPickDate.setOnClickListener {
            val dialog = UIUtils.createDatePickerDialog(requireContext(), this)
            dialog.show(parentFragmentManager, DATE_PICKER_DIALOG_TAG)
        }

        diaryViewModel.consumedFoods.observe(viewLifecycleOwner, { diaryEntries ->
            diaryEntries?.let {
                diaryEntryAdapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val formattedDate = DateFormatter.formatToValidDate(year, monthOfYear + 1, dayOfMonth) // valid format means: yyyy-MM-dd
        CurrentDate.currentDate = formattedDate
        diaryViewModel.changeDate(CurrentDate.currentDate)
        binding.pickedDate = formattedDate
    }
}