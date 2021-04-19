package com.android.project.lightweight.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.data.adapter.NutrientAdapter
import com.android.project.lightweight.data.viewmodel.DetailsViewModel
import com.android.project.lightweight.databinding.FragmentDetailsBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry
import com.android.project.lightweight.util.AppConstants
import com.android.project.lightweight.util.UIUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment @Inject constructor(private val nutrientAdapter: NutrientAdapter) : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var diaryEntry: DiaryEntry
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this
        handleNavigationArguments()
        setupRecyclerView()
        setupToolbar()
        updateNutrientSummary()
        setupChipClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.nutrients.observe(viewLifecycleOwner, { nutrientEntryList ->
            nutrientEntryList?.let {
                nutrientAdapter.setNutrients(it)
            }
        })
    }

    private fun handleNavigationArguments() {
        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            diaryEntry = argsBundle.diaryEntry
            if (diaryEntry.id == 0L) { // this means that the entry is not in database yet
                nutrientAdapter.setNutrients(diaryEntry.nutrientEntries)
            } else {
                detailsViewModel.setDiaryEntry(diaryEntry) // this will trigger a room query from detailsViewModel
            }
            binding.diaryEntry = diaryEntry
        }
    }

    private fun setupRecyclerView() {
        binding.foodNutrientsRecyclerView.apply {
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    // Live update to nutrients if value is changing in the editText
    private fun updateNutrientSummary() {
        binding.edtConsumedAmount.doOnTextChanged { _: CharSequence?, _: Int, _: Int, _: Int ->
            val amountValue = binding.edtConsumedAmount.text.toString()
            if (amountValue.isNotEmpty()) {
                detailsViewModel.updateDiaryEntry(diaryEntry, amountValue.toInt())
                nutrientAdapter.setNutrients(diaryEntry.nutrientEntries)
            }
        }
    }

    private fun setupChipClickListener() {
        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
                val filteredNutrients = if (diaryEntry.id == 0L) {
                    filterByNutrient(chip, diaryEntry.nutrientEntries)
                } else {
                    filterByNutrient(chip, detailsViewModel.nutrients.value!!)
                }
                nutrientAdapter.setNutrients(filteredNutrients)
            }
        }
    }

    private fun filterByNutrient(view: View, nutrientEntries: List<NutrientEntry>): List<NutrientEntry> {
        return when (view.id) {
            R.id.chip_general -> detailsViewModel.filter(nutrientEntries, AppConstants.general)
            R.id.chip_vitamins -> detailsViewModel.filter(nutrientEntries, AppConstants.vitamins)
            R.id.chip_minerals -> detailsViewModel.filter(nutrientEntries, AppConstants.minerals)
            else -> {
                nutrientEntries
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_diary_menu -> {
                saveDiaryEntry()
                true
            }
            R.id.delete_diary_menu -> {
                deleteDiaryEntry()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveDiaryEntry() {
        val consumedAmountText = binding.edtConsumedAmount.text.toString()
        if (consumedAmountText.isNotEmpty()) {
            detailsViewModel.saveDiaryEntry(diaryEntry)
            UIUtils.createAnchoredSnackbar(requireActivity(), getString(R.string.diaryentry_added_snackbar_text), Snackbar.LENGTH_SHORT).show()
        } else {
            UIUtils.createAnchoredSnackbar(requireActivity(), getString(R.string.please_add_consumption_amount), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun deleteDiaryEntry() {
        detailsViewModel.deleteDiaryEntry(diaryEntry.id)
        UIUtils.createAnchoredSnackbar(requireActivity(), getString(R.string.diaryentry_removed_snackbar_text), Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
    }
}