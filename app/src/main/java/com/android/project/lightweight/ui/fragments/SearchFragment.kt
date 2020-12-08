package com.android.project.lightweight.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.api.model.Food
import com.android.project.lightweight.data.SearchViewModel
import com.android.project.lightweight.data.adapters.FoodAdapter
import com.android.project.lightweight.data.adapters.OnFoodClickListener
import com.android.project.lightweight.databinding.FragmentSearchBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.transformer.EntityTransformer
import com.android.project.lightweight.ui.onQueryTextChanged
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter
import com.android.project.lightweight.utilities.UIUtils

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private val foodAdapter by lazy {
        FoodAdapter(object : OnFoodClickListener {
            override fun onClick(food: Food) {
                val diaryEntry = DiaryEntry(food.fdcId, food.description, DateFormatter.parseDateToLong(CurrentDate.currentDate))
                diaryEntry.nutrients = EntityTransformer.transformFoodNutrientsToNutrientEntries(food.foodNutrients, diaryEntry.id)
                UIUtils.closeKeyboard(requireActivity())
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(diaryEntry))
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = searchViewModel

        binding.foodRecyclerView.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
        }
        binding.searchView.queryHint = getString(R.string.what_did_i_consume)// TODO: This should work from XML?!
        binding.searchView.onQueryTextChanged {
            binding.progressbar.visibility = View.VISIBLE
            searchViewModel.getFoods(it)
        }

        searchViewModel.response.observe(viewLifecycleOwner, {
            it?.let {
                foodAdapter.submitList(it)
                binding.progressbar.visibility = View.INVISIBLE
                binding.noResultsTextview.visibility = when (it.isEmpty()) {
                    true -> View.VISIBLE
                    else -> View.INVISIBLE
                }
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }
}