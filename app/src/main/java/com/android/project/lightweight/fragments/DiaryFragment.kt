package com.android.project.lightweight.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.data.DiaryViewModel
import com.android.project.lightweight.data.adapters.FoodAdapter
import com.android.project.lightweight.data.adapters.OnFoodClickListener
import com.android.project.lightweight.databinding.FragmentDiaryBinding
import com.android.project.lightweight.network.Food
import com.android.project.lightweight.utilities.AppConstants
import com.android.project.lightweight.utilities.UIUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

class DiaryFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentDiaryBinding
    private var date = 0L
    private val navController by lazy {
        findNavController()
    }
    private val foodAdapter by lazy {
        FoodAdapter(object : OnFoodClickListener {
            override fun onClick(food: Food) {
                navController.navigate(DiaryFragmentDirections.actionDiaryFragmentToDetailsFragment(food, "DiaryFragment", date))
            }
        })
    }

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(this).get(DiaryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        binding.lifecycleOwner = this
        date = AppConstants.TODAY_FORMATTED
        binding.diaryRecyclerview.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
        }

        arguments?.let { bundle ->
            // if the bundle is not empty the consumedFood which sent back from DetailsFragment exists
            // purpose of this was testing, it will be handled by Room and LiveData later.
            if (!bundle.isEmpty) {
                val args = DiaryFragmentArgs.fromBundle(bundle)
                val food = args.consumedFood
                Toast.makeText(requireContext(), "Food name:" + food.description, Toast.LENGTH_LONG).show()
            }
        }

//        diaryViewModel.consumedFoods.observe(viewLifecycleOwner, {
//            it?.let {
//                foodAdapter.submitList(it)
//            }
//        })

        binding.btnPickDate.setOnClickListener {
            val dialog = UIUtils.createDatePickerDialog(requireContext(), this)
            dialog.show(parentFragmentManager, "Datepickerdialog")
        }

        return binding.root
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // The picked date in the given format will be handled by [DateBindingAdapter]
        val str = "$year-${monthOfYear + 1}-$dayOfMonth"
        date = str.toLong()
        binding.pickedDate = str
    }
}