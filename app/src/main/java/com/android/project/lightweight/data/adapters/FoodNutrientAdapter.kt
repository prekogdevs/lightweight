package com.android.project.lightweight.data.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.databinding.ListItemFoodNutrientBinding
import com.android.project.lightweight.network.FoodNutrient

class FoodNutrientAdapter(private var foodNutrients: List<FoodNutrient>) : RecyclerView.Adapter<FoodNutrientAdapter.FoodNutrientHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodNutrientHolder {
        return FoodNutrientHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FoodNutrientHolder, position: Int) {
        val foodNutrient = foodNutrients[position]
        holder.bindFoodNutrient(foodNutrient)
    }

    override fun getItemCount() = foodNutrients.size

    fun setNutrients(foodNutrients: List<FoodNutrient>) {
        this.foodNutrients = foodNutrients
        notifyDataSetChanged()
    }

    class FoodNutrientHolder private constructor(private val binding: ListItemFoodNutrientBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindFoodNutrient(foodNutrient: FoodNutrient) {
            binding.foodNutrient = foodNutrient
        }

        companion object {
            fun from(parent: ViewGroup): FoodNutrientHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFoodNutrientBinding.inflate(layoutInflater, parent, false)
                return FoodNutrientHolder(binding)
            }
        }
    }
}