package com.android.project.lightweight.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.databinding.ListItemFoodNutrientBinding
import com.android.project.lightweight.persistence.entity.NutrientEntry

class NutrientAdapter : RecyclerView.Adapter<NutrientAdapter.FoodNutrientHolder>() {
    private var nutrientEntries = listOf<NutrientEntry>()

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