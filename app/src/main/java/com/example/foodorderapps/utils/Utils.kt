package com.example.foodorderapps.utils

import java.time.LocalDateTime

class Utils {
    companion object {
        const val USERS = "Users"
        const val ADMIN = "Admins"
        val PROFILE = LocalDateTime.now()!!
        const val HOME_FRAGMENT = "home"
        const val PROFILE_FRAGMENT = "profile"
    }
}