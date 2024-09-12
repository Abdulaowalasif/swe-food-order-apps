package com.example.foodorderapps.admin.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapps.R
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.databinding.ActivityAdminHomeBinding
import com.example.foodorderapps.user.activities.Login
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminHome : AppCompatActivity() {
    val binding: ActivityAdminHomeBinding by lazy {
        ActivityAdminHomeBinding.inflate(layoutInflater)
    }
    private val adminAuth: AdminAuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.logout.setOnClickListener {
            adminAuth.logout()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}