package com.android.project.lightweight.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.api.retrofit.model.Food
import com.android.project.lightweight.databinding.ListItemFoodBinding

class FoodAdapter : ListAdapter<Food, FoodAdapter.FoodHolder>(FoodCallback()) {

    private lateinit var listener: OnFoodClickListener
    fun setOnItemClickListener(onItemClickListener: OnFoodClickListener) {
        listener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        return FoodHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val food = getItem(position)
        holder.bindFood(food, listener)
    }

    class FoodHolder private constructor(private val binding: ListItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindFood(food: Food, listener: OnFoodClickListener) {
            binding.food = food
            binding.listener = listener
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
    override fun areItemsTheSame(oldItem: Food, newItem: Food) =
        oldItem.fdcId == newItem.fdcId

    override fun areContentsTheSame(oldItem: Food, newItem: Food) =
        oldItem == newItem
}

interface OnFoodClickListener {
    fun onClick(food: Food)
}