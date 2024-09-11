package com.example.foodorderapps.user.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.R
import com.example.foodorderapps.admin.activities.AdminHome
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.user.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private val auth: AdminAuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.decorView.windowInsetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if the user is logged in as an admin

            // Check if the user is logged in as a regular user
            if (viewModel.getCurrentUser() != null) {
                val intent = Intent(this, MainScreen::class.java)
                startActivity(intent)
            }
            // If neither admin nor user is logged in, navigate to login screen
            else {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
            // Close the current activity
            finish()
        }, 3000)



    }
}