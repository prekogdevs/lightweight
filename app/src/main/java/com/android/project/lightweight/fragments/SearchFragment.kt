package com.android.project.lightweight.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.data.SearchViewModel
import com.android.project.lightweight.data.adapters.FoodAdapter
import com.android.project.lightweight.data.adapters.OnFoodClickListener
import com.android.project.lightweight.databinding.FragmentSearchBinding
import com.android.project.lightweight.network.Food
import com.android.project.lightweight.utilities.UIUtils

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private val foodAdapter by lazy {
        FoodAdapter(object : OnFoodClickListener {
            override fun onClick(food: Food) {
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(food))
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = searchViewModel
        binding.btnSearch.setOnClickListener {
            val foodName = binding.edtFood.text.toString()
            searchViewModel.getFoods("DEMO_KEY", foodName)
            UIUtils.closeKeyboard(requireActivity())
            binding.noResultsTextview.visibility = View.INVISIBLE
            binding.progressbar.visibility = View.VISIBLE
        }

        binding.foodRecyclerView.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
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

        return binding.root
    }
}