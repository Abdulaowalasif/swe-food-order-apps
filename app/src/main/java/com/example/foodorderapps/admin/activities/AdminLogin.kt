package com.example.foodorderapps.admin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.databinding.ActivityAdminLoginBinding
import com.example.foodorderapps.user.activities.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminLogin : AppCompatActivity() {
    private val binding:ActivityAdminLoginBinding by lazy {
        ActivityAdminLoginBinding.inflate(layoutInflater)
    }

    private val auth: AdminAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            binding.progressBar2.visibility = View.VISIBLE
            if (email.isNotBlank() && pass.isNotBlank()) {
                auth.signInAdmin(email, pass)
            } else {
                binding.progressBar2.visibility = View.GONE
                Toast.makeText(this, "enter all the details", Toast.LENGTH_SHORT).show()
            }

        }
        auth.signInAdminState.observe(this) {
            if (it.isSuccess) {
                binding.progressBar2.visibility = View.GONE
                val intent = Intent(this, MainScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.progressBar2.visibility = View.GONE
                Toast.makeText(
                    this,
                    auth.signInAdminState.value.toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
}