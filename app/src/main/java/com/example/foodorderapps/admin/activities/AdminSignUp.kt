package com.example.foodorderapps.admin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.databinding.ActivityAdminSignUpBinding
import com.example.foodorderapps.user.activities.Login
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminSignUp : AppCompatActivity() {
    private val binding: ActivityAdminSignUpBinding by lazy {
        ActivityAdminSignUpBinding.inflate(layoutInflater)
    }
    private val authViewModel: AdminAuthViewModel by viewModels()
    private lateinit var imagePicker: ActivityResultLauncher<String>
    private var imageUri = "no image uploaded."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imagePicker = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                imageUri = uri.toString()
                binding.profileImage.setImageURI(uri)
            }
        }

        binding.profileImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.signUp.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val username = binding.username.text.toString()
            binding.progressBar.visibility = View.VISIBLE
            if (email.isNotBlank() && pass.isNotBlank() && username.isNotBlank()) {
                authViewModel.signUpAdmin(
                    email = email,
                    password = pass,
                    restaurantName = username,
                    image = imageUri
                )
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter all the details", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.signUpAdminState.observe(this) {
            if (it.isSuccess) {
                binding.progressBar.visibility = View.GONE
                val intent = Intent(this, AdminLogin::class.java)
                startActivity(intent)
                finish()

            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, AdminLogin::class.java)
            startActivity(intent)
            finish()
        }

    }
}