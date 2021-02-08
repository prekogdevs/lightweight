package com.android.project.lightweight.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.data.DetailsViewModel
import com.android.project.lightweight.data.adapters.NutrientAdapter
import com.android.project.lightweight.databinding.FragmentDetailsBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.ui.handleExpansion
import com.android.project.lightweight.utilities.UIUtils.closeKeyboard
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.toolbar_with_textview.view.*

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var diaryEntry: DiaryEntry
    private val navController by lazy { findNavController() }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private var nutrientAdapter = NutrientAdapter(mutableListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            diaryEntry = argsBundle.diaryEntry
            binding.includedLayout.toolbarTextView.text = getString(R.string.nutrients_in_food, diaryEntry.description)
            if (diaryEntry.id == 0L) { // this means that the entry is not in database yet
                nutrientAdapter.setNutrients(diaryEntry.nutrients)
                binding.btnPersistFood.text = getString(R.string.save_text)
            } else {
                detailsViewModel.getNutrientEntriesByDiaryEntryId(diaryEntry.id) // this will initiate a room query from detailsViewModel
                binding.btnPersistFood.text = getString(R.string.remove_text)
            }
            binding.diaryEntry = diaryEntry
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }

        binding.edtConsumedAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val amountValue = binding.edtConsumedAmount.text.toString()
                if (amountValue.isNotEmpty()) {
                    val consumedNutrientsBasedOnAmount = detailsViewModel.calculateConsumedNutrients(diaryEntry, amountValue.toInt())
                    diaryEntry.nutrients = consumedNutrientsBasedOnAmount
                    nutrientAdapter.setNutrients(consumedNutrientsBasedOnAmount)
                }
            }
        })

        detailsViewModel.nutrients.observe(viewLifecycleOwner, { nutrientEntryList ->
            nutrientEntryList?.let {
                nutrientAdapter.setNutrients(it)
            }
        })

        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
                val filteredNutrients = if (diaryEntry.id == 0L) {
                    detailsViewModel.filterFoodNutrients(chip, diaryEntry.nutrients)
                } else {
                    detailsViewModel.filterFoodNutrients(chip, detailsViewModel.nutrients.value!!)
                }
                nutrientAdapter.setNutrients(filteredNutrients)
            }
        }

        binding.btnPersistFood.setOnClickListener {
            if (diaryEntry.id == 0L) {
                closeKeyboard(requireActivity())
                val amountValue = binding.edtConsumedAmount.text.toString()
                if (amountValue.isNotEmpty()) {
                    // Saving new entry
                    diaryEntry.consumedAmount = binding.edtConsumedAmount.text.toString().toInt()
                    diaryEntry.unitName = "g"
                    diaryEntry.kcal = detailsViewModel.energyInFood(diaryEntry.nutrients)
                    diaryEntry.protein = detailsViewModel.proteinInFood(diaryEntry.nutrients)
                    diaryEntry.carbs = detailsViewModel.carbsInFood(diaryEntry.nutrients)
                    diaryEntry.fats = detailsViewModel.fatsInFood(diaryEntry.nutrients)
                    detailsViewModel.insertDiaryEntryWithNutrientEntries(diaryEntry)
                    Snackbar.make(requireView(), "Diary entry has been saved", Snackbar.LENGTH_SHORT).show()
                    navController.navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
                } else {
                    Snackbar.make(requireView(), "Please add consumption amount", Snackbar.LENGTH_SHORT).show()
                }
                // Removing entry
            } else {
                detailsViewModel.deleteDiaryEntry(diaryEntry.id)
                Snackbar.make(requireView(), "Diary entry has been removed", Snackbar.LENGTH_SHORT).show()
                navController.navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
            }
        }
        binding.txtNutrientSummary.setOnClickListener {
            binding.nutrientSummaryExpandableLayout.handleExpansion(it as TextView)
        }
        return binding.root
    }

    // Source: https://developer.android.com/guide/navigation/navigation-ui#support_app_bar_variations
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appBarConfig = AppBarConfiguration(navController.graph)
        view.includedLayout.toolbar.setupWithNavController(navController, appBarConfig)
    }
}