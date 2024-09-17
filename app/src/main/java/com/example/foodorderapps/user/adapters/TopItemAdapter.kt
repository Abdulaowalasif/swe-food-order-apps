package com.example.foodorderapps.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.databinding.TopItemListBinding
import com.example.foodorderapps.databinding.ActivityAdminHomeBinding.bind
import com.example.foodorderapps.user.activities.AllItem

class TopItemAdapter(private var items: List<MenuList>?) :
    RecyclerView.Adapter<TopItemAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = TopItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        val intent = Intent(context, AllItem::class.java)
        intent.putExtra("topItem", item)
        context.startActivity(intent)
    }

    inner class ViewHolder(private val binding: TopItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuList) {
            Glide.with(context).load(item.image).into(binding.topItemImage)
            binding.topItemTitle.text = item.name
        }
    }
}