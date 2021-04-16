package com.android.project.lightweight.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
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
import com.android.project.lightweight.persistence.transformer.EntityTransformer
import com.android.project.lightweight.util.AppConstants
import com.android.project.lightweight.util.CurrentDate
import com.android.project.lightweight.util.DateFormatter
import com.android.project.lightweight.util.UIUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val detailsViewModel: DetailsViewModel by viewModels()
    private var nutrientAdapter = NutrientAdapter()
    private var diaryEntry = DiaryEntry()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this
        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            detailsViewModel.triggerInitialization(argsBundle.diaryEntry, argsBundle.food)
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }

        detailsViewModel.diaryEntryWithNutrients.observe(viewLifecycleOwner, { diaryEntryWithNutrients ->
            diaryEntryWithNutrients?.let {
                diaryEntry = it.diaryEntry
                diaryEntry.nutrientEntries = it.nutrients
                binding.diaryEntry = diaryEntry
                nutrientAdapter.setNutrients(diaryEntry.nutrientEntries)
            }
        })

        detailsViewModel.foodie.observe(viewLifecycleOwner, { diaryEntryWithNutrients ->
            diaryEntryWithNutrients?.let {
                val nutrients = EntityTransformer.transformFoodNutrientsToNutrientEntries(DateFormatter.parseDateToLong(CurrentDate.currentDate), it.foodNutrients)
                nutrientAdapter.setNutrients(nutrients)
            }
        })


        // TODO: Add Delay
        binding.edtConsumedAmount.doOnTextChanged { _: CharSequence?, _: Int, _: Int, _: Int ->
            val amountValue = binding.edtConsumedAmount.text.toString()
            if (amountValue.isNotEmpty()) {
//                detailsViewModel.updateNutrients(amountValue.toInt())
//                nutrientAdapter.setNutrients(diaryEntry.nutrientEntries)
            }
        }

        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
                val filteredNutrients = filterByNutrient(chip, diaryEntry.nutrientEntries)
                nutrientAdapter.setNutrients(filteredNutrients)
            }
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

    private fun saveDiaryEntry() {
        val consumedAmountText = binding.edtConsumedAmount.text.toString()
        if (consumedAmountText.isNotEmpty()) {
            UIUtils.closeKeyboard(requireActivity())
//            detailsViewModel.setConsumptionDetails(consumedAmountText.toInt()) // TODO: Update and not set
            detailsViewModel.saveDiaryEntry()
            Snackbar.make(requireView(), getString(R.string.diaryentry_added_snackbar_text), Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
        } else {
            Snackbar.make(requireView(), getString(R.string.amount_missing_snackbar_text), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun deleteDiaryEntry() {
        detailsViewModel.deleteDiaryEntry()
        Snackbar.make(requireView(), getString(R.string.diaryentry_removed_snackbar_text), Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
    }
}