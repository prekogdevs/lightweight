package com.android.project.lightweight.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.project.lightweight.R
import com.android.project.lightweight.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this

        // TODO: For test purposes only. Remove it later.
        arguments?.let { bundle ->
            val food = DetailsFragmentArgs.fromBundle(bundle).selectedFood
            var text = ""
            for (nutrient in food.foodNutrients) {
                text += "Nutrient name: " + nutrient.nutrientName + ",unit name: " + nutrient.unitName + ", amount: " + nutrient.amount + "\n"
            }
            binding.txtTest.text = text
        }

        return binding.root
    }

}