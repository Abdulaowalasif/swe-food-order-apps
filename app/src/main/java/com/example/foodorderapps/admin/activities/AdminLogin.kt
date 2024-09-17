package com.example.foodorderapps.admin.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.common.utils.Utils.Companion.navigateToNext
import com.example.foodorderapps.databinding.ActivityAdminLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminLogin : AppCompatActivity() {
    private val binding: ActivityAdminLoginBinding by lazy {
        ActivityAdminLoginBinding.inflate(layoutInflater)
    }

    private val authViewModel: AdminAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotBlank() && pass.isNotBlank()) {
                binding.progressBar2.visibility = View.VISIBLE
                authViewModel.signInAdmin(email, pass)
            } else {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.signInAdminState.observe(this) { result ->
            binding.progressBar2.visibility = View.GONE
            if (result.isSuccess) {
                navigateToNext(this,AdminHome::class.java)
                finish()
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpText.setOnClickListener {
            navigateToNext(this,AdminSignUp::class.java)
        }

        // Handle back press to exit the app
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}
