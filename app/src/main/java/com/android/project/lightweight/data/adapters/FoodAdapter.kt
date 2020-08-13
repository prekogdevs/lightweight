package com.android.project.lightweight.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.databinding.ListItemFoodBinding
import com.android.project.lightweight.network.Food

class FoodAdapter : ListAdapter<Food, FoodAdapter.FoodHolder>(FoodCallback()) {

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val food = getItem(position)
        holder.bindFood(food)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        return FoodHolder.from(parent)
    }

    class FoodHolder private constructor(private val binding: ListItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindFood(food: Food) {
            binding.food = food
        }

        companion object {
            fun from(parent: ViewGroup): FoodHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFoodBinding.inflate(layoutInflater, parent, false)
                return FoodHolder(binding)
            }
        }

    }
}

class FoodCallback : DiffUtil.ItemCallback<Food>() {
    override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem.fdcId == newItem.fdcId
    }
    override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem == newItem
    }
}