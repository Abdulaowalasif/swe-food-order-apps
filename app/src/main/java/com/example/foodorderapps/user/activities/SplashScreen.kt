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

    private val user: AuthViewModel by viewModels()
    private val admin: AdminAuthViewModel by viewModels()

    private var adminUid: String = ""
    private var userUid: String = ""

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.decorView.windowInsetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        admin.uid()
        user.uid()

        admin.uid.observe(this) { uid ->
            if (uid.isNotBlank()) {
                admin.adminExist(uid)
                admin.adminExist.observe(this) {
                    if (it) {
                        adminUid = uid
                    }
                }
            }
        }
        user.uid.observe(this) { uid ->
            if (uid.isNotBlank()) {
                user.checkUser(uid)
                user.userExist.observe(this) {
                    if (it) {
                        userUid = uid
                    }
                }
            }
        }


        Handler(Looper.getMainLooper()).postDelayed({
            if (adminUid != "") {
                val intent = Intent(this, AdminHome::class.java)
                startActivity(intent)
                finish()
            } else if (userUid != "") {
                val intent = Intent(this, MainScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)


    }
}