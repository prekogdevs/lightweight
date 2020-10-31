package com.android.project.lightweight.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.project.lightweight.databinding.ListItemDiaryEntryBinding
import com.android.project.lightweight.persistence.entity.DiaryEntry

class DiaryEntryAdapter(var listener: OnDiaryEntryClickListener) : ListAdapter<DiaryEntry, DiaryEntryAdapter.DiaryEntryHolder>(DiaryEntryCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryEntryHolder {
        return DiaryEntryHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DiaryEntryHolder, position: Int) {
        val entry = getItem(position)
        holder.bindFood(entry, listener)
    }

    class DiaryEntryHolder private constructor(private val binding: ListItemDiaryEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindFood(diaryEntry: DiaryEntry, listener: OnDiaryEntryClickListener) {
            binding.diaryEntry = diaryEntry
            binding.listener = listener
        }

        companion object {
            fun from(parent: ViewGroup): DiaryEntryHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDiaryEntryBinding.inflate(layoutInflater, parent, false)
                return DiaryEntryHolder(binding)
            }
        }

    }
}


class DiaryEntryCallback : DiffUtil.ItemCallback<DiaryEntry>() {
    override fun areItemsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
        return oldItem == newItem
    }
}

interface OnDiaryEntryClickListener {
    fun onClick(diaryEntry: DiaryEntry)
}