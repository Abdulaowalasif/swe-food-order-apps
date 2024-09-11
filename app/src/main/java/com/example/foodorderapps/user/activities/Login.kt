package com.example.foodorderapps.user.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.admin.activities.AdminLogin
import com.example.foodorderapps.databinding.ActivityLoginBinding
import com.example.foodorderapps.user.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            binding.progressBar2.visibility = View.VISIBLE
            if (email.isNotBlank() && pass.isNotBlank()) {
                authViewModel.signInUser(email, pass)
            } else {
                binding.progressBar2.visibility = View.GONE
                Toast.makeText(this, "enter all the details", Toast.LENGTH_SHORT).show()
            }

        }
        authViewModel.signInUserState.observe(this) {
            if (it.isSuccess) {
                binding.progressBar2.visibility = View.GONE
                val intent = Intent(this, MainScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.progressBar2.visibility = View.GONE
                Toast.makeText(
                    this,
                    authViewModel.signInUserState.value.toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        binding.signUpText.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        binding.adminLogin.setOnClickListener {
            val intent=Intent(this,AdminLogin::class.java)
            startActivity(intent)
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

}