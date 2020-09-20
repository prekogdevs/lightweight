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
import com.android.project.lightweight.factory.ViewModelFactory
import com.android.project.lightweight.network.Food
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var food: Food
    private val navController by lazy {
        findNavController()
    }
    private lateinit var viewModelFactory: ViewModelFactory
    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    private val foodNutrientAdapter by lazy {
        FoodNutrientAdapter(food.foodNutrients)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        arguments?.let { bundle ->
            val argsBundle = DetailsFragmentArgs.fromBundle(bundle)
            food = argsBundle.selectedFood
            binding.previousFragment = argsBundle.previousFragment // This value defines the visibility of Save button (handled in fragment_details.xml with databinding)
            binding.includedLayout.toolbarTextView.text = getString(R.string.nutrients_in_food, food.description)
        }
        binding.foodNutrientsRecyclerView.apply {
            adapter = foodNutrientAdapter
            setHasFixedSize(true)
        }

        val application = requireNotNull(activity).application
        viewModelFactory = ViewModelFactory(application, food)
        binding.viewModel = detailsViewModel

        binding.chipGroup.forEach {
            it.setOnClickListener { chip ->
                foodNutrientAdapter.setNutrients(detailsViewModel.filterNutrients(chip))
            }
        }

        binding.btnSaveFood.setOnClickListener{

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