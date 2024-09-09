package com.example.foodorderapps.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapps.databinding.RestaurantBinding
import com.example.foodorderapps.user.models.Restaurant

class RestaurantAdapter(private val restaurant: List<Restaurant>) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = restaurant[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = restaurant.size

    inner class ViewHolder(private val binding: RestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Restaurant) {
            binding.restaurantImage.setImageResource(item.image)
            binding.restaurantName.text = item.name
        }
    }
}
