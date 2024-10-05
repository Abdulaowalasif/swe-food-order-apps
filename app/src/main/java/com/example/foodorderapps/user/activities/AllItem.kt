package com.example.foodorderapps.user.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.databinding.ActivityAllItemBinding
import com.example.foodorderapps.user.adapters.AllItemAdapter
import com.example.foodorderapps.user.adapters.RestaurantAdapter
import com.example.foodorderapps.user.viewModels.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllItem : AppCompatActivity() {
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    private val dataViewModel: DataViewModel by viewModels()
    private val adapter = AllItemAdapter()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.allItemRc.adapter = adapter
        val item = intent.getSerializableExtra("item") as? Restaurants

        item?.let {
            binding.restaurantName.text = it.name
            Glide.with(this).load(it.image).into(binding.restaurantImage)
            dataViewModel.fetchMenuById(it.id)
        }

        lifecycleScope.launch {
            dataViewModel.menuList.collect { list ->
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
            }
        }

    }
}