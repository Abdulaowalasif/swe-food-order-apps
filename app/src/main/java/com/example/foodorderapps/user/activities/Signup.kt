package com.example.foodorderapps.user.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.databinding.ActivitySignupBinding
import com.example.foodorderapps.user.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Signup : AppCompatActivity() {
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val authViewModel: AuthViewModel by viewModels()
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
                authViewModel.signUpUser(email, pass, username, image = imageUri)
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter all the details", Toast.LENGTH_SHORT).show()
            }
        }
        authViewModel.signUpUserState.observe(this) {
            if (it.isSuccess) {
                binding.progressBar.visibility = View.GONE
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


    }
}