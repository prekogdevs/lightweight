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
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

// TODO: Remove !! checks
//  https://kotlinlang.org/docs/reference/whatsnew12.html#checking-whether-a-lateinit-var-is-initialized
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private var food: Food? = null
    private var diaryEntry: DiaryEntry? = null
    private val navController by lazy { findNavController() }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private var nutrientAdapter = FoodNutrientAdapter(emptyList())

    private var consumedOn = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            consumedOn = argsBundle.consumedOn
            food = argsBundle.food
            diaryEntry = argsBundle.diaryEntry
            when (food) { // with this check we know which was the previous fragment (when food is null: previousFragment = "DiaryFragment" else "SearchFragment")
                null -> {
                    detailsViewModel.setDiaryEntryId(diaryEntry!!.id)
                    // TODO: Extract XML "code"
                    binding.includedLayout.toolbarTextView.text = getString(R.string.nutrients_in_food, diaryEntry!!.description)
                    binding.btnPersistFood.text = "Remove"
                }
                else -> {
                    food = argsBundle.food!!
                    detailsViewModel.setFood(food!!)
                    // TODO: Extract XML "code"
                    binding.includedLayout.toolbarTextView.text = getString(R.string.nutrients_in_food, food!!.description)
                    binding.btnPersistFood.text = "Save"
                }
            }
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }

        detailsViewModel.nutrients.observe(viewLifecycleOwner, { nutrientEntryList ->
            nutrientEntryList?.let {
                nutrientAdapter.setNutrients(EntityTransformer.transformNutrientToFoodNutrient(detailsViewModel.nutrients.value!!.filter { it.consumedAmount > 0 }))
            }
        })

        detailsViewModel.food.observe(viewLifecycleOwner, { food ->
            food?.let {
                nutrientAdapter.setNutrients(food.foodNutrients.filter { it.amount > 0 })
            }
        })

        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
                when(food) {
                    null -> {
                        // TODO: Refactor
                        nutrientAdapter.setNutrients(detailsViewModel.filterFoodNutrients(chip, EntityTransformer.transformNutrientToFoodNutrient(detailsViewModel.nutrients.value!!)))
                    }
                    else -> {
                        nutrientAdapter.setNutrients(detailsViewModel.filterFoodNutrients(chip, food!!.foodNutrients))
                    }
                }
            }
        }

        binding.btnPersistFood.setOnClickListener {
            when (food) {
                null ->
                    detailsViewModel.deleteDiaryEntry(diaryEntry!!.fdcId, consumedOn)
                else -> {
                    val diaryEntry = DiaryEntry(food!!.fdcId, food!!.description, consumedOn)
                    detailsViewModel.insertDiaryEntryWithNutrientEntries(diaryEntry, food!!.foodNutrients)
                }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        (activity as MainActivity).showBottomNavigation()
        super.onDetach()
    }
}