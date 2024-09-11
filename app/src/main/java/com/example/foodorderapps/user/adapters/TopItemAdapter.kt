package com.example.foodorderapps.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapps.databinding.TopItemListBinding
import com.example.foodorderapps.common.models.TopItems

class TopItemAdapter(private val items: List<TopItems>) :
    RecyclerView.Adapter<TopItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TopItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: TopItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TopItems) {
            binding.topItemImage.setImageResource(item.image)
            binding.topItemTitle.text = item.title
        }
    }
}