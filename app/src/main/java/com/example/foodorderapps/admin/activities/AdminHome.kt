package com.example.foodorderapps.admin.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapps.R
import com.example.foodorderapps.databinding.ActivityAdminHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminHome : AppCompatActivity() {
    val binding:ActivityAdminHomeBinding by lazy {
        ActivityAdminHomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}