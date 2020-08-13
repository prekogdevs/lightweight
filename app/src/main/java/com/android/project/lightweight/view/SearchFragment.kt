package com.android.project.lightweight.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.project.lightweight.R
import com.android.project.lightweight.data.SearchViewModel
import com.android.project.lightweight.data.adapters.FoodAdapter
import com.android.project.lightweight.databinding.FragmentSearchBinding
import com.android.project.lightweight.utilities.UIUtils
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }
    private val foodAdapter by lazy {
        FoodAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = searchViewModel

        binding.btnSearch.setOnClickListener{
            searchViewModel.getFoods("DEMO_KEY", edtFood.text.toString())
            UIUtils.closeKeyboard(requireActivity())
        }

        binding.foodRV.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
        }

        searchViewModel.response.observe(viewLifecycleOwner, Observer {
            it?.let {
                foodAdapter.submitList(it)
            }
        })

        return binding.root
    }
}