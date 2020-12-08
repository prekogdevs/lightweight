package com.android.project.lightweight.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.android.project.lightweight.utilities.UIUtils.closeKeyboard
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.toolbar_with_textview.view.*

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var diaryEntry: DiaryEntry
    private val navController by lazy { findNavController() }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private var nutrientAdapter = NutrientAdapter(emptyList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            diaryEntry = argsBundle.diaryEntry
            binding.includedLayout.toolbarTextView.text = getString(R.string.nutrients_in_food, diaryEntry.description)
            if (diaryEntry.id == 0L) { // this means that the entry is not in database yet
                nutrientAdapter.setNutrients(diaryEntry.nutrients)
                binding.btnPersistFood.text = getString(R.string.saveText)
            } else {
                detailsViewModel.getNutrientEntriesByDiaryEntryId(diaryEntry.id) // this will initiate a room query from detailsViewModel
                binding.btnPersistFood.text = getString(R.string.removeText)
            }
            binding.diaryEntry = diaryEntry
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }

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
                diaryEntry.consumedAmount = binding.edtConsumedAmount.text.toString().toInt() // TODO: Handle empty edittext
                // TODO: For testing purposes (refactor later)
                diaryEntry.unitName = "g"
                diaryEntry.kcal = detailsViewModel.energyInFood(diaryEntry.nutrients)
                detailsViewModel.insertDiaryEntryWithNutrientEntries(diaryEntry)
            } else {
                detailsViewModel.deleteDiaryEntry(diaryEntry.id)
            }
            navController.navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
        }

        return binding.root
    }

    // Source: https://developer.android.com/guide/navigation/navigation-ui#support_app_bar_variations
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appBarConfig = AppBarConfiguration(navController.graph)
        view.includedLayout.toolbar.setupWithNavController(navController, appBarConfig)
    }
}