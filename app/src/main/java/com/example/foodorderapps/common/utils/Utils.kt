package com.example.foodorderapps.common.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime

class Utils {
    companion object {
        const val USERS = "Users"
        const val ADMIN = "Admins"
        val PROFILE = LocalDateTime.now()!!
        const val HOME_FRAGMENT = "home"
        const val PROFILE_FRAGMENT = "profile"
        const val BASE_URL = "http://192.168.1.205:8080/"
        const val ITEM_REF="Items"

        fun navigateToNext(
            context: Context,
            destination: Class<out AppCompatActivity>
        ) {
            val intent = Intent(context, destination)
            context.startActivity(intent)
        }

    }
}