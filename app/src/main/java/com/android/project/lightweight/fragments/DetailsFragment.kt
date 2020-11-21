package com.android.project.lightweight.fragments

import android.content.Context
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
import com.android.project.lightweight.MainActivity
import com.android.project.lightweight.R
import com.android.project.lightweight.api.model.Food
import com.android.project.lightweight.data.DetailsViewModel
import com.android.project.lightweight.data.adapters.FoodNutrientAdapter
import com.android.project.lightweight.databinding.FragmentDetailsBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.transformer.EntityTransformer
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var diaryEntry: DiaryEntry
    private val navController by lazy { findNavController() }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private var nutrientAdapter = FoodNutrientAdapter(emptyList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            diaryEntry = argsBundle.diaryEntry
            binding.includedLayout.toolbarTextView.text = getString(R.string.nutrients_in_food, diaryEntry.description)
            binding.btnPersistFood.text = if (diaryEntry.id == 0L) getString(R.string.saveText) else getString(R.string.removeText)
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }

//        detailsViewModel.nutrients.observe(viewLifecycleOwner, { nutrientEntryList ->
//            nutrientEntryList?.let {
//                nutrientAdapter.setNutrients(EntityTransformer.transformNutrientToFoodNutrient(detailsViewModel.nutrients.value!!.filter { it.consumedAmount > 0 }))
//            }
//        })
//
//        detailsViewModel.food.observe(viewLifecycleOwner, { food ->
//            food?.let {
//                nutrientAdapter.setNutrients(food.foodNutrients.filter { it.amount > 0 })
//            }
//        })

        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
//                when(food) {
//                    null -> {
//                        // TODO: Refactor
//                        nutrientAdapter.setNutrients(detailsViewModel.filterFoodNutrients(chip, EntityTransformer.transformNutrientToFoodNutrient(detailsViewModel.nutrients.value!!)))
//                    }
//                    else -> {
//                        nutrientAdapter.setNutrients(detailsViewModel.filterFoodNutrients(chip, food!!.foodNutrients))
//                    }
//                }
            }
        }

        binding.btnPersistFood.setOnClickListener {
            if (diaryEntry.id == 0L) {
                detailsViewModel.insertDiaryEntryWithNutrientEntries(diaryEntry)
            } else {
                detailsViewModel.deleteDiaryEntry(diaryEntry.id)
            }
//            when (food) {
//                null ->
//                    detailsViewModel.deleteDiaryEntry(diaryEntry!!.fdcId, DateFormatter.parseDateToLong(CurrentDate.currentDate))
//                else -> {
//                    val diaryEntry = DiaryEntry(food!!.fdcId, food!!.description, DateFormatter.parseDateToLong(CurrentDate.currentDate))
//                    detailsViewModel.insertDiaryEntryWithNutrientEntries(diaryEntry, food!!.foodNutrients)
//                }
//            }
            navController.navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment())
        }

        return binding.root
    }

    // Source: https://developer.android.com/guide/navigation/navigation-ui#support_app_bar_variations
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appBarConfig = AppBarConfiguration(navController.graph)
        view.includedLayout.toolbar.setupWithNavController(navController, appBarConfig)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        (activity as MainActivity).showBottomNavigation()
        super.onDetach()
    }
}