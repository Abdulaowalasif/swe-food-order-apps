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
import com.example.foodorderapps.common.utils.Utils.Companion.ITEM_DETAILS
import com.example.foodorderapps.databinding.ItemDetailsBinding
import com.example.foodorderapps.user.activities.ItemDetails

class AllItemAdapter : ListAdapter<MenuList, AllItemAdapter.ViewHolder>(MenuUtil()) {

    inner class ViewHolder(
        private val binding: ItemDetailsBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuList) {
            binding.itemTitle.text = item.name
            binding.itemPrice.text = item.price.toString()
            Glide.with(context).load(item.image).into(binding.image)
            itemView.setOnClickListener {
                val intent = Intent(context, ItemDetails::class.java)
                intent.putExtra(ITEM_DETAILS, item)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MenuUtil : DiffUtil.ItemCallback<MenuList>() {
        override fun areItemsTheSame(oldItem: MenuList, newItem: MenuList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuList, newItem: MenuList): Boolean {
            return oldItem == newItem
        }


    }
}