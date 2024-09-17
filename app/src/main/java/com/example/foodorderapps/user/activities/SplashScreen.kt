package com.example.foodorderapps.user.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapps.R
import com.example.foodorderapps.admin.activities.AdminHome
import com.example.foodorderapps.admin.viewmodels.AdminAuthViewModel
import com.example.foodorderapps.common.utils.Utils.Companion.navigateToNext
import com.example.foodorderapps.user.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private val userViewModel: AuthViewModel by viewModels()
    private val adminViewModel: AdminAuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        hideSystemBars()

        val user = userViewModel.getCurrentUser()
        val email = user?.email

        if (email != null) {
            userViewModel.checkUser(email)
            userViewModel.userExist.observe(this) { userExists ->
                if (userExists) {
                    navigateToNext(this, MainScreen::class.java)
                    finish()
                } else {
                    adminViewModel.adminExist(email)
                    adminViewModel.adminExist.observe(this) { adminExists ->
                        if (adminExists) {
                            navigateToNext(this, AdminHome::class.java)
                            finish()
                        } else {
                            navigateToNext(this, Login::class.java)
                            finish()
                        }
                    }
                }
            }
        } else {
            navigateToNext(this, Login::class.java)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemBars() {
        window.decorView.windowInsetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}
