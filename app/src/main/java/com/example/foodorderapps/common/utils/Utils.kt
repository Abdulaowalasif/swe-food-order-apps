package com.example.foodorderapps.common.utils

import java.time.LocalDateTime

class Utils {
    companion object {
        const val USERS = "Users"
        const val ADMIN = "Admins"
        val PROFILE = LocalDateTime.now()!!
        const val HOME_FRAGMENT = "home"
        const val PROFILE_FRAGMENT = "profile"
        const val BASE_URL = "http://192.168.0.179:8080/"
    }
}