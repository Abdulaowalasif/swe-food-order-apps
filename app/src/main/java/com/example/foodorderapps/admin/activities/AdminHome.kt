package com.example.foodorderapps.admin.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.admin.viewmodels.AdminDataViewmodel
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.common.utils.Utils
import com.example.foodorderapps.databinding.ActivityAdminHomeBinding
import com.example.foodorderapps.user.adapters.RestaurantAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminHome : AppCompatActivity() {
    val binding: ActivityAdminHomeBinding by lazy {
        ActivityAdminHomeBinding.inflate(layoutInflater)
    }
    private val auth: AdminAuthViewModel by viewModels()
    private val data: AdminDataViewmodel by viewModels()
    private var uid: String = ""
    private val adapter = RestaurantAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth.getCurrentAdminData()
        uid = auth.getCurrentAdmin()?.uid!!
        data.getAllMenu(uid)
        auth.adminInfo.observe(this) { info ->
            binding.title.text = info.username
            Glide.with(this).load(info.image).into(binding.profileImage)
        }

        binding.apply {
            adminRc.adapter = adapter

            addMenu.setOnClickListener {
                Utils.navigateToNext(this@AdminHome, Items::class.java)
            }

            lifecycleScope.launch {
                data.allMenuResult.collect { it ->
                    val allItem = ArrayList<Restaurants>()
                    it?.forEach { eItem ->
                        val items = Restaurants(id = uid, name = eItem.name, image = eItem.image)
                        allItem.add(items)

                    }
                    adapter.submitList(allItem)
                    adapter.notifyDataSetChanged()
                }
            }

        }

    }
}