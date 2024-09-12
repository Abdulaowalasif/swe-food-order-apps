package com.example.foodorderapps.user.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.common.models.Admin
import com.example.foodorderapps.user.repositories.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: UserRepo) : ViewModel() {

    private val _signUpUserState = MutableLiveData<Result<Unit>>()
    val signUpUserState: LiveData<Result<Unit>> = _signUpUserState
    private val _signInUserState = MutableLiveData<Result<Unit>>()
    val signInUserState: LiveData<Result<Unit>> = _signInUserState
    private val _userInfo = MutableLiveData<Admin>()
    val userInfo: LiveData<Admin> = _userInfo
    private val _uid = MutableLiveData<String>()
    val uid: LiveData<String> = _uid
    private val _userExist = MutableLiveData<Boolean>()
    val userExist: LiveData<Boolean> = _userExist


    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            _signInUserState.value = repo.signInUser(email, password)
        }
    }

    fun signUpUser(email: String, password: String, username: String, image: String) {
        viewModelScope.launch {
            _signUpUserState.value = repo.signUpUser(email, password, username, image)
        }
    }

    fun getCurrentUser() = repo.getCurrentUser()

    fun getCurrentUserInfo() =
        viewModelScope.launch {
            _userInfo.postValue(repo.getCurrentUserData())
        }

    fun logout() = repo.logOut()


    fun uid() {
        viewModelScope.launch {
            _uid.value = repo.uid()
        }
    }

    fun checkUser(uid: String) {
        viewModelScope.launch {
            _userExist.value = repo.checkUser(uid)
        }
    }
}


