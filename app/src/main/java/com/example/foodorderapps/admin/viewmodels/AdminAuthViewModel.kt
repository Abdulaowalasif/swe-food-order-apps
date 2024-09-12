package com.example.foodorderapps.admin.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.admin.repositories.AdminRepositories
import com.example.foodorderapps.common.models.Admin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAuthViewModel @Inject constructor(private val repo: AdminRepositories) : ViewModel() {

    private val _signUpAdminState = MutableLiveData<Result<Unit>>()
    val signUpAdminState: LiveData<Result<Unit>> get() = _signUpAdminState

    private val _signInAdminState = MutableLiveData<Result<Unit>>()
    val signInAdminState: LiveData<Result<Unit>> get() = _signInAdminState

    private val _adminInfo = MutableLiveData<Admin>()
    val adminInfo: LiveData<Admin> get() = _adminInfo

    private val _adminExist = MutableLiveData<Boolean>()
    val adminExist: LiveData<Boolean> = _adminExist

    private val _uid = MutableLiveData<String>()
    val uid: LiveData<String> = _uid


    fun signInAdmin(email: String, password: String) {
        viewModelScope.launch {
            try {
                _signInAdminState.value = repo.signInAdmin(email, password)
            } catch (e: Exception) {
                _signInAdminState.value = Result.failure(e)
                Log.e("SignIn Error", "Error during sign-in: ${e.message}", e)
            }
        }
    }

    fun signUpAdmin(email: String, password: String, restaurantName: String, image: String) {
        viewModelScope.launch {
            try {
                _signUpAdminState.value = repo.signUpAdmin(email, password, restaurantName, image)

            } catch (e: Exception) {
                _signUpAdminState.value = Result.failure(e)
                Log.e("SignUp Error", "Error during sign-up: ${e.message}", e)
            }
        }
    }

    fun getCurrentAdmin() {
        viewModelScope.launch {
            try {
                _adminInfo.value = repo.getCurrentAdminData()
            } catch (e: Exception) {
                Log.e("AdminInfo Error", "Error fetching admin info: ${e.message}", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repo.logOut()
            } catch (e: Exception) {
                Log.e("Logout Error", "Error during logout: ${e.message}", e)
            }
        }
    }

    fun adminExist(uid: String) {
        viewModelScope.launch {
            _adminExist.value = repo.checkAdmin(uid)
        }
    }

    fun uid() {
        viewModelScope.launch {
            _uid.value = repo.uid()
        }
    }

}
