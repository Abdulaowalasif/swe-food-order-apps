package com.example.foodorderapps.user.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.databinding.SearchLayoutBinding

class SearchAdapter(private val items: List<MenuList>?) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(private val binding: SearchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuList) {
            Glide.with(context).load(item.image).into(binding.searchImage)
            binding.searchText.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SearchLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}

