package com.android.project.lightweight.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.project.lightweight.R
import com.android.project.lightweight.data.adapters.FoodAdapter
import com.android.project.lightweight.data.adapters.OnFoodClickListener
import com.android.project.lightweight.databinding.FragmentDiaryBinding
import com.android.project.lightweight.network.Food
import com.android.project.lightweight.utilities.UIUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

class DiaryFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentDiaryBinding
    private val navController by lazy {
        findNavController()
    }
    private val foodAdapter by lazy {
        FoodAdapter(object : OnFoodClickListener {
            override fun onClick(food: Food) {
                navController.navigate(DiaryFragmentDirections.actionDiaryFragmentToDetailsFragment(food, "DiaryFragment"))
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        binding.lifecycleOwner = this
        binding.btnPickDate.setOnClickListener {
            val dialog = UIUtils.createDatePickerDialog(requireContext(), this)
            dialog.show(parentFragmentManager, "Datepickerdialog")
        }

        val foods = listOf(Food(1, "Fincsi kaja", listOf()), Food(2, "Fincsi kaja2", listOf()), Food(3, "Fincsi kaja3", listOf()))
        foodAdapter.submitList(foods)
        binding.diaryRecyclerview.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
        }

        return binding.root
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // The picked date in the given format will be handled by [DateBindingAdapter]
        binding.pickedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
    }
}