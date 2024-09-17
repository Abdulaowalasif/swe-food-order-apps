package com.example.foodorderapps.admin.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.admin.viewmodels.AdminDataViewmodel
import com.example.foodorderapps.databinding.ActivityAdminHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminHome : AppCompatActivity() {
    val binding: ActivityAdminHomeBinding by lazy {
        ActivityAdminHomeBinding.inflate(layoutInflater)
    }
    private val adminAuth: AdminAuthViewModel by viewModels()
    private val data: AdminDataViewmodel by viewModels()
    private var uid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adminAuth.getCurrentAdminData()
        uid = adminAuth.getCurrentAdmin()?.uid!!
        adminAuth.adminInfo.observe(this) { info ->
            binding.title.text = info.username
            Glide.with(this).load(info.image).into(binding.profileImage)
        }

        binding.addMenu.setOnClickListener {
        }


    }
}