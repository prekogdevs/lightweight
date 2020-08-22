package com.android.project.lightweight.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.data.adapters.FoodNutrientAdapter
import com.android.project.lightweight.databinding.FragmentDetailsBinding
import com.android.project.lightweight.network.Food
import kotlinx.android.synthetic.main.fragment_details.view.*

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var food: Food
    private lateinit var navController: NavController
    private val foodNutrientAdapter by lazy {
        FoodNutrientAdapter(food.foodNutrients)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this
        navController = findNavController()
        arguments?.let { bundle ->
            food = DetailsFragmentArgs.fromBundle(bundle).selectedFood
            binding.toolbarTextView.text = getString(R.string.nutrients_in_food, food.description)
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = foodNutrientAdapter
            setHasFixedSize(true)
        }
        return binding.root
    }

    // Source: https://developer.android.com/guide/navigation/navigation-ui#support_app_bar_variations
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appBarConfig = AppBarConfiguration(navController.graph)
        view.toolbar.setupWithNavController(navController, appBarConfig)
    }
}