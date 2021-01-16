package com.android.project.lightweight.data.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.databinding.ListItemFoodNutrientBinding
import com.android.project.lightweight.persistence.entity.NutrientEntry

//TODO: ListAdapter?
class NutrientAdapter(private var nutrientEntries: List<NutrientEntry>) : RecyclerView.Adapter<NutrientAdapter.FoodNutrientHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodNutrientHolder {
        return FoodNutrientHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FoodNutrientHolder, position: Int) {
        val foodNutrient = nutrientEntries[position]
        holder.bindFoodNutrient(foodNutrient)
    }

    override fun getItemCount() = nutrientEntries.size

    fun setNutrients(nutrientEntries: List<NutrientEntry>) {
        this.nutrientEntries = nutrientEntries
        notifyDataSetChanged()
    }

    class FoodNutrientHolder private constructor(private val binding: ListItemFoodNutrientBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindFoodNutrient(nutrientEntry: NutrientEntry) {
            binding.nutrientEntry = nutrientEntry
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