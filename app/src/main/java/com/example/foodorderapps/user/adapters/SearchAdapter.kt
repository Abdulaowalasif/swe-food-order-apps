package com.example.foodorderapps.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.databinding.SearchLayoutBinding

class SearchAdapter :
    ListAdapter<MenuList, SearchAdapter.ViewHolder>(MenuListDiffCallback()) {

    inner class ViewHolder(private val binding: SearchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuList, context: Context) {
            Glide.with(context).load(item.image).into(binding.searchImage)
            binding.searchText.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SearchLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val context = holder.itemView.context
        holder.bind(item, context)
    }

    class MenuListDiffCallback : DiffUtil.ItemCallback<MenuList>() {
        override fun areItemsTheSame(oldItem: MenuList, newItem: MenuList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuList, newItem: MenuList): Boolean {
            return oldItem == newItem
        }
    }
}
