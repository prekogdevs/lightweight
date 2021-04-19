package com.android.project.lightweight.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.R
import com.android.project.lightweight.data.adapter.DiaryEntryAdapter
import com.android.project.lightweight.data.adapter.OnDiaryEntryClickListener
import com.android.project.lightweight.data.viewmodel.DiaryViewModel
import com.android.project.lightweight.databinding.FragmentDiaryBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.ui.extension.handleExpansion
import com.android.project.lightweight.util.AppConstants.DATE_PICKER_DIALOG_TAG
import com.android.project.lightweight.util.CurrentDate
import com.android.project.lightweight.util.DateFormatter
import com.android.project.lightweight.util.UIUtils
import com.google.android.material.snackbar.Snackbar
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiaryFragment @Inject constructor(val diaryEntryAdapter: DiaryEntryAdapter) : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentDiaryBinding
    private val diaryViewModel: DiaryViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        binding.lifecycleOwner = this
        binding.pickedDate = CurrentDate.currentDate
        binding.diaryViewModel = diaryViewModel
        setupRecyclerView()
        setClickListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        diaryViewModel.consumedFoods.observe(viewLifecycleOwner, { diaryEntries ->
            diaryEntries?.let {
                diaryEntryAdapter.submitList(it)
            }
        })
    }

    private fun setClickListeners() {
        binding.consumedNutrientsSummaryIncluded.txtNutrientSummary.setOnClickListener {
            binding.consumedNutrientsSummaryIncluded.nutrientSummaryExpandableLayout.handleExpansion(it as TextView)
        }

        binding.btnPickDate.setOnClickListener {
            val dialog = UIUtils.createDatePickerDialog(requireContext(), this)
            dialog.show(parentFragmentManager, DATE_PICKER_DIALOG_TAG)
        }
    }

    private fun setupRecyclerView() {
        diaryEntryAdapter.setOnItemClickListener(
            object : OnDiaryEntryClickListener {
                override fun onClick(diaryEntry: DiaryEntry) {
                    findNavController().navigate(DiaryFragmentDirections.actionDiaryFragmentToDetailsFragment(diaryEntry))
                }
            }
        )
        binding.diaryRecyclerview.apply {
            adapter = diaryEntryAdapter
            setHasFixedSize(true)
        }

        // Swipe to delete functionality
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = true
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val diaryEntry = diaryEntryAdapter.currentList[position]
                diaryViewModel.deleteDiaryEntry(diaryEntry.id)
                UIUtils.createAnchoredSnackbar(requireActivity(), getString(R.string.diaryentry_removed_snackbar_text), Snackbar.LENGTH_SHORT).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.diaryRecyclerview)
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val formattedDate = DateFormatter.formatToValidDate(year, monthOfYear + 1, dayOfMonth) // valid format means: yyyy-MM-dd
        CurrentDate.currentDate = formattedDate
        diaryViewModel.changeDate(CurrentDate.currentDate)
        binding.pickedDate = formattedDate
    }
}