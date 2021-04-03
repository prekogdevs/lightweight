package com.android.project.lightweight.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.android.project.lightweight.R
import com.android.project.lightweight.api.retrofit.model.Food
import com.android.project.lightweight.data.adapter.FoodAdapter
import com.android.project.lightweight.data.adapter.OnFoodClickListener
import com.android.project.lightweight.data.viewmodel.SearchViewModel
import com.android.project.lightweight.databinding.FragmentSearchBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.transformer.EntityTransformer
import com.android.project.lightweight.ui.extension.onQueryTextChanged
import com.android.project.lightweight.util.CurrentDate
import com.android.project.lightweight.util.DateFormatter
import com.android.project.lightweight.util.UIUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    private val foodAdapter by lazy {
        FoodAdapter(object : OnFoodClickListener {
            override fun onClick(food: Food) {
                val filtered = searchViewModel.filterOutNotRequiredNutrients(food)
                val consumedOn = DateFormatter.parseDateToLong(CurrentDate.currentDate)
                val diaryEntry = DiaryEntry(food.fdcId, food.description, consumedOn)
                diaryEntry.nutrients = EntityTransformer.transformFoodNutrientsToNutrientEntries(filtered, diaryEntry)
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
            // to detect when new items were added, and then scroll to the top of the recyclerview
            foodAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    smoothScrollToPosition(0)
                }
            })
        }
        binding.searchView.apply {
            queryHint = getString(R.string.what_did_i_consume)
            onQueryTextChanged {
                binding.progressbar.visibility = View.VISIBLE
                binding.emptyResultImage.visibility = View.GONE
                searchViewModel.searchForFood(it)
            }
        }
        searchViewModel.foodResponse.observe(viewLifecycleOwner, { event ->
            val foodResponse = event.peekContent().data
            foodResponse?.let {
                foodAdapter.submitList(it.foods)
                binding.progressbar.visibility = View.INVISIBLE
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }
}