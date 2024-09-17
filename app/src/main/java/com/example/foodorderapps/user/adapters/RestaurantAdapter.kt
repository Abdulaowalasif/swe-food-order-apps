package com.example.foodorderapps.user.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapps.databinding.RestaurantBinding
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.user.activities.AllItem

class RestaurantAdapter(private var restaurants: List<Restaurants>?) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun getItemCount(): Int = restaurants?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = restaurants?.get(position)
        if (item != null) {
            holder.bind(item)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AllItem::class.java)
            intent.putExtra("item", item)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(private val binding: RestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Restaurants) {
            Glide.with(context).load(Uri.parse(item.image)).into(binding.restaurantImage)
            binding.restaurantName.text = item.name
        }
    }
}
