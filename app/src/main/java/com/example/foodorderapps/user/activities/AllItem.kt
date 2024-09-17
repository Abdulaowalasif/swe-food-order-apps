package com.example.foodorderapps.user.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.databinding.ActivityAllItemBinding
import com.example.foodorderapps.user.viewModels.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllItem : AppCompatActivity() {
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    private val dataViewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val item = intent.getSerializableExtra("item") as? Restaurants

        item?.let {
            binding.restaurantName.text = it.name
            Glide.with(this).load(it.image).into(binding.restaurantImage)
            dataViewModel.fetchMenuById(it.id)
        }

        lifecycleScope.launch {
            dataViewModel.menuList.collect {
                binding.item.text = it.toString()
            }
        }

    }
}