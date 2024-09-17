package com.example.foodorderapps.common.di

import android.app.Application
import androidx.activity.viewModels
import com.example.foodorderapps.user.viewModels.AuthViewModel
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class UserApplication : Application()