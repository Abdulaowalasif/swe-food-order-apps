package com.example.foodorderapps.admin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.admin.repositories.AdminRepositories
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdminDataViewmodel @Inject constructor(private val repo: AdminRepositories) :ViewModel() {
    private val _menuResult = MutableLiveData<Result<MenuList?>>()
    val menuResult: LiveData<Result<MenuList?>> get() = _menuResult

    private val _deleteMenuResult = MutableLiveData<Result<Boolean>>()
    val deleteMenuResult: LiveData<Result<Boolean>> get() = _deleteMenuResult

    private val _allMenuResult = MutableLiveData<Result<List<MenuList>?>>()
    val allMenuResult: LiveData<Result<List<MenuList>?>> get() = _allMenuResult

    private val _deleteRestaurantsResult = MutableLiveData<Result<Boolean>>()
    val deleteRestaurantsResult: LiveData<Result<Boolean>> get() = _deleteRestaurantsResult

    private val _orderResult = MutableLiveData<Result<List<OrderList>?>>()
    val orderResult: LiveData<Result<List<OrderList>?>> get() = _orderResult

    private val _deleteOrderResult = MutableLiveData<Result<Boolean>>()
    val deleteOrderResult: LiveData<Result<Boolean>> get() = _deleteOrderResult


    fun addMenu(menuList: MenuList) {
        viewModelScope.launch {
            try {
                val result = repo.addMenu(menuList)
                _menuResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _menuResult.postValue(Result.failure(e))
            }
        }
    }

    fun deleteMenu(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.deleteMenu(id)
                _deleteMenuResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _deleteMenuResult.postValue(Result.failure(e))
            }
        }
    }

    fun getAllMenu(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.getAllMenu(id)
                _allMenuResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _allMenuResult.postValue(Result.failure(e))
            }
        }
    }

    fun deleteRestaurants(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.deleteRestaurants(id)
                _deleteRestaurantsResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _deleteRestaurantsResult.postValue(Result.failure(e))
            }
        }
    }

    fun getOrder(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.getAllOrders(id)
                _orderResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _orderResult.postValue(Result.failure(e))
            }
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.deleteOrder(id)
                _deleteOrderResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _deleteOrderResult.postValue(Result.failure(e))
            }
        }
    }
}