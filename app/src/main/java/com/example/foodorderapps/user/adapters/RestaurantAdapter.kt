package com.example.foodorderapps.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapps.databinding.RestaurantBinding
import com.example.foodorderapps.common.models.Restaurant // Ensure this is the correct model
import com.example.foodorderapps.common.models.Restaurants

class RestaurantAdapter(private var restaurants: List<Restaurants>?) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRestaurants: List<Restaurants>?) {
        restaurants = newRestaurants
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = restaurants?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = restaurants?.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class ViewHolder(private val binding: RestaurantBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Restaurants) {
            Glide.with(context).load(Uri.parse(item.image)).into(binding.restaurantImage)
            binding.restaurantName.text = item.name
        }
    }
}
