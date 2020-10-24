package com.android.project.lightweight.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.project.lightweight.MainActivity
import com.android.project.lightweight.R
import com.android.project.lightweight.data.DiaryViewModel
import com.android.project.lightweight.data.adapters.FoodAdapter
import com.android.project.lightweight.data.adapters.OnFoodClickListener
import com.android.project.lightweight.databinding.FragmentDiaryBinding
import com.android.project.lightweight.persistence.entity.Food
import com.android.project.lightweight.utilities.CurrentDate
import com.android.project.lightweight.utilities.DateFormatter
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
                navController.navigate(DiaryFragmentDirections.actionDiaryFragmentToDetailsFragment(food, "DiaryFragment", DateFormatter.parseDateToLong(CurrentDate.currentDate)))
            }
        })
    }
    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(this).get(DiaryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        binding.lifecycleOwner = this
        binding.pickedDate = CurrentDate.currentDate
        binding.diaryRecyclerview.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
        }

        diaryViewModel.consumedFoods.observe(viewLifecycleOwner, {
            it?.let {
                foodAdapter.submitList(it)
            }
        })

        binding.btnPickDate.setOnClickListener {
            val dialog = UIUtils.createDatePickerDialog(requireContext(), this)
            dialog.show(parentFragmentManager, "Datepickerdialog")
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onDetach() {
        (activity as MainActivity).hideBottomNavigation()
        super.onDetach()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // The picked date in the given format will be handled by [DateBindingAdapter]
        val str = "$year-${monthOfYear + 1}-$dayOfMonth"
        CurrentDate.currentDate = str
        diaryViewModel.changeDate(CurrentDate.currentDate)
        binding.pickedDate = str
    }
}