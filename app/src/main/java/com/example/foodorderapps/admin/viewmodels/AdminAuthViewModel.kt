package com.example.foodorderapps.admin.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.admin.repositories.AdminRepositories
import com.example.foodorderapps.admin.models.Admins
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAuthViewModel @Inject constructor(private val repo: AdminRepositories) : ViewModel() {

    private val _signUpAdminState = MutableLiveData<Result<Unit>>()
    val signUpAdminState = _signUpAdminState
    private val _signInAdminState = MutableLiveData<Result<Unit>>()
    val signInAdminState = _signInAdminState
    private val _adminInfo = MutableLiveData<Admins>()
    val adminInfo = _adminInfo


    fun signInAdmin(email: String, password: String) {
        viewModelScope.launch {
            _signInAdminState.value = repo.signInAdmin(email, password)
        }
    }

    fun signUpAdmin(email: String, password: String, restaurantName: String, image: String) {
        viewModelScope.launch {
            _signUpAdminState.value = repo.signUpAdmin(email, password, restaurantName, image)
        }
    }

    fun getCurrentAdmin() = repo.getCurrentAdmin()

    fun getCurrentAdminInfo() =
        viewModelScope.launch {
            _adminInfo.postValue(repo.getCurrentAdminData())
        }

    fun logout() = repo.logOut()
}


