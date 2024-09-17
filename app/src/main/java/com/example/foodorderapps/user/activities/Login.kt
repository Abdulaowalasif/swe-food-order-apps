package com.example.foodorderapps.user.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.admin.activities.AdminLogin
import com.example.foodorderapps.common.utils.Utils.Companion.navigateToNext
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
                navigateToNext(this, MainScreen::class.java)
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
            navigateToNext(this, Signup::class.java)
        }

        binding.adminLogin.setOnClickListener {
            navigateToNext(this, AdminLogin::class.java)
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

}