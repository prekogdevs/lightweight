package com.android.project.lightweight.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.R
import com.android.project.lightweight.api.retrofit.model.Food
import com.android.project.lightweight.data.adapter.FoodAdapter
import com.android.project.lightweight.data.adapter.OnFoodClickListener
import com.android.project.lightweight.data.viewmodel.SearchViewModel
import com.android.project.lightweight.databinding.FragmentSearchBinding
import com.android.project.lightweight.ui.extension.onQueryTextChanged
import com.android.project.lightweight.util.AppConstants.SEARCH_FOR_FOOD_DELAY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment @Inject constructor(val foodAdapter: FoodAdapter) : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.searchViewModel = searchViewModel
        setupRecyclerView()
        setupSearchView()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.foodResponse.observe(viewLifecycleOwner, { resource ->
            val foodResponse = resource.data
            foodResponse?.let {
                foodAdapter.submitList(it.foods)
            }
        })
    }

    private fun setupRecyclerView() {
        foodAdapter.setOnItemClickListener(object : OnFoodClickListener {
            override fun onClick(food: Food) {
                val diaryEntry = searchViewModel.createDiaryEntryFromFood(food)
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(diaryEntry))
            }
        })
        binding.foodRecyclerView.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
            // to detect when new items were added, and then scroll to the top of the recyclerview
            foodAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    smoothScrollToPosition(0)
                }
            })
        }
    }

    private fun setupSearchView() {
        var searchJob: Job? = null
        binding.searchView.apply {
            onQueryTextChanged { query ->
                searchJob?.cancel()
                searchJob = MainScope().launch {
                    delay(SEARCH_FOR_FOOD_DELAY)
                    searchViewModel.searchForFood(query)
                }
            }
        }
    }
}