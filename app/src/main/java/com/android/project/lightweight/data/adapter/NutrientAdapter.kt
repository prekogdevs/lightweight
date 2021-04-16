package com.android.project.lightweight.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.databinding.ListItemFoodNutrientBinding
import com.android.project.lightweight.persistence.entity.NutrientEntry

class NutrientAdapter : RecyclerView.Adapter<NutrientAdapter.NutrientEntryHolder>() {
    private var nutrientEntries = listOf<NutrientEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientEntryHolder {
        return NutrientEntryHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NutrientEntryHolder, position: Int) {
        val foodNutrient = nutrientEntries[position]
        holder.bindFoodNutrient(foodNutrient)
    }

    override fun getItemCount() = nutrientEntries.size

    fun setNutrients(nutrientEntries: List<NutrientEntry>) {
        this.nutrientEntries = nutrientEntries
        notifyDataSetChanged()
    }

    class NutrientEntryHolder private constructor(private val binding: ListItemFoodNutrientBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindFoodNutrient(nutrientEntry: NutrientEntry) {
            binding.nutrientEntry = nutrientEntry
        }

        companion object {
            fun from(parent: ViewGroup): NutrientEntryHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFoodNutrientBinding.inflate(layoutInflater, parent, false)
                return NutrientEntryHolder(binding)
            }
        }
    }
}