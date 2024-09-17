package com.example.foodorderapps.user.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapps.databinding.RestaurantBinding
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.user.activities.AllItem

class RestaurantAdapter :
    ListAdapter<Restaurants, RestaurantAdapter.ViewHolder>(RestaurantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: RestaurantBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Restaurants) {
            Glide.with(context).load(Uri.parse(item.image)).into(binding.restaurantImage)
            binding.restaurantName.text = item.name

            // Set up the click listener
            itemView.setOnClickListener {
                val intent = Intent(context, AllItem::class.java)
                intent.putExtra("item", item)
                context.startActivity(intent)
            }
        }
    }

    // Define a DiffUtil.ItemCallback to calculate differences between lists
    class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurants>() {
        override fun areItemsTheSame(oldItem: Restaurants, newItem: Restaurants): Boolean {
            // Assuming Restaurants has an id or unique identifier
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurants, newItem: Restaurants): Boolean {
            return oldItem == newItem
        }
    }

}
