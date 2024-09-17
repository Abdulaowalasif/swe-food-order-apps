package com.example.foodorderapps.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.databinding.TopItemListBinding
import com.example.foodorderapps.user.activities.AllItem

class TopItemAdapter :
    ListAdapter<MenuList, TopItemAdapter.ViewHolder>(MenuListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TopItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: TopItemListBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuList) {
            Glide.with(context).load(item.image).into(binding.topItemImage)
            binding.topItemTitle.text = item.name

            // Set up the click listener
            itemView.setOnClickListener {
                val intent = Intent(context, AllItem::class.java)
                intent.putExtra("topItem", item)
                context.startActivity(intent)
            }
        }
    }

    class MenuListDiffCallback : DiffUtil.ItemCallback<MenuList>() {
        override fun areItemsTheSame(oldItem: MenuList, newItem: MenuList): Boolean {
            // Assuming MenuList has an id or unique identifier
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuList, newItem: MenuList): Boolean {
            return oldItem == newItem
        }
    }
}
