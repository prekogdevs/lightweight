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
import com.android.project.lightweight.data.DetailsViewModel
import com.android.project.lightweight.data.adapters.FoodNutrientAdapter
import com.android.project.lightweight.databinding.FragmentDetailsBinding
import com.android.project.lightweight.persistence.entity.Food
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var food: Food
    private val navController by lazy {
        findNavController()
    }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private val foodNutrientAdapter by lazy {
        FoodNutrientAdapter(food.foodNutrients)
    }

    private var consumedWhen = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            food = argsBundle.selectedFood
            consumedWhen = argsBundle.consumedWhen
            binding.previousFragment = argsBundle.previousFragment // This value defines the visibility of Save button (handled in fragment_details.xml with databinding)
            binding.includedLayout.toolbarTextView.text = getString(R.string.nutrients_in_food, food.description)
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = foodNutrientAdapter
            setHasFixedSize(true)
        }

        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
                foodNutrientAdapter.setNutrients(detailsViewModel.filterNutrients(chip, food))
            }
        }

        binding.btnPersistFood.setOnClickListener {
            if (binding.previousFragment == "SearchFragment") {
                detailsViewModel.insert(Food(food.fdcId, food.description, food.foodNutrients, consumedWhen))
            } else {
                detailsViewModel.delete(food)
            }
            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToDiaryFragment(food))
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