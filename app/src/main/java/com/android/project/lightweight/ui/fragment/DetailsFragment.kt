package com.android.project.lightweight.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.android.project.lightweight.ui.extension.handleExpansion
import com.android.project.lightweight.util.AppConstants
import com.android.project.lightweight.util.UIUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var diaryEntry: DiaryEntry
    private val navController by lazy { findNavController() }
    private val detailsViewModel: DetailsViewModel by viewModels()
    private var nutrientAdapter = NutrientAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            diaryEntry = argsBundle.diaryEntry
            if (diaryEntry.id == 0L) { // this means that the entry is not in database yet
                nutrientAdapter.setNutrients(diaryEntry.nutrients)
            } else {
                detailsViewModel.setDiaryEntryId(diaryEntry.id) // this will trigger a room query from detailsViewModel
            }
            binding.diaryEntry = diaryEntry
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }

        // Live update to nutrients if value is changing in the editText
        binding.edtConsumedAmount.doOnTextChanged { _: CharSequence?, _: Int, _: Int, _: Int ->
            val amountValue = binding.edtConsumedAmount.text.toString()
            if (amountValue.isNotEmpty()) {
                val consumedNutrientsBasedOnAmount = detailsViewModel.calculateConsumedNutrients(diaryEntry.nutrients, amountValue.toInt())
                diaryEntry.nutrients = consumedNutrientsBasedOnAmount
                nutrientAdapter.setNutrients(consumedNutrientsBasedOnAmount)
            }
        }

        detailsViewModel.nutrients.observe(viewLifecycleOwner, { nutrientEntryList ->
            nutrientEntryList?.let {
                nutrientAdapter.setNutrients(it)
            }
        })

        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
                val filteredNutrients = if (diaryEntry.id == 0L) {
                    filterByNutrient(chip, diaryEntry.nutrients)
                } else {
                    filterByNutrient(chip, detailsViewModel.nutrients.value!!)
                }
                nutrientAdapter.setNutrients(filteredNutrients)
            }
        }
        binding.txtNutrientSummary.setOnClickListener {
            binding.nutrientSummaryExpandableLayout.handleExpansion(it as TextView)
        }
        binding.toolbar.apply {
            inflateMenu(R.menu.details_menu)
            setOnMenuItemClickListener {
                onOptionsItemSelected(it)
            }
            val menuItem = menu.findItem(R.id.action)
            if (diaryEntry.id == 0L) menuItem.setIcon(R.drawable.ic_save_24)
            else menuItem.setIcon(R.drawable.ic_remove_24)
        }

        return binding.root
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
            R.id.action -> {
                if (diaryEntry.id == 0L) saveDiaryEntry() // New entry
                else deleteFood()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveDiaryEntry() {
        val consumedAmountText = binding.edtConsumedAmount.text.toString()
        if (consumedAmountText.isNotEmpty()) {
            UIUtils.closeKeyboard(requireActivity())
            detailsViewModel.setConsumptionDetails(diaryEntry, consumedAmountText.toInt())
            detailsViewModel.saveDiaryEntry(diaryEntry)
            Snackbar.make(requireView(), getString(R.string.diaryentry_added_snackbar_text), Snackbar.LENGTH_SHORT).show()
            navController.navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
        } else {
            Snackbar.make(requireView(), getString(R.string.amount_missing_snackbar_text), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun deleteFood() {
        detailsViewModel.deleteDiaryEntry(diaryEntry.id)
        Snackbar.make(requireView(), getString(R.string.diaryentry_removed_snackbar_text), Snackbar.LENGTH_SHORT).show()
        navController.navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
    }
}