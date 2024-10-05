package com.example.foodorderapps.admin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.admin.repositories.AdminRepositories
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdminDataViewmodel @Inject constructor(private val repo: AdminRepositories) : ViewModel() {
    private val _menuResult = MutableStateFlow<Result<MenuList?>>(Result.success(null))
    val menuResult: StateFlow<Result<MenuList?>> get() = _menuResult

    private val _deleteMenuResult = MutableStateFlow(Result.success(false))
    val deleteMenuResult: StateFlow<Result<Boolean>> get() = _deleteMenuResult

    private val _allMenuResult = MutableStateFlow<List<MenuList>?>(null)
    val allMenuResult: StateFlow<List<MenuList>?> get() = _allMenuResult

    private val _deleteRestaurantsResult = MutableStateFlow(Result.success(false))
    val deleteRestaurantsResult: StateFlow<Result<Boolean>> get() = _deleteRestaurantsResult

    private val _orderResult = MutableStateFlow<Result<List<OrderList>?>>(Result.success(null))
    val orderResult: StateFlow<Result<List<OrderList>?>> get() = _orderResult

    private val _deleteOrderResult = MutableStateFlow<Result<Boolean>>(Result.success(false))
    val deleteOrderResult: StateFlow<Result<Boolean>> get() = _deleteOrderResult


    fun addMenu(menuList: MenuList) {
        viewModelScope.launch {
            try {
                val result = repo.addMenu(menuList)
                _menuResult.emit(Result.success(result))
            } catch (e: Exception) {
                _menuResult.emit(Result.failure(e))
            }
        }
    }

    fun deleteMenu(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.deleteMenu(id)
                _deleteMenuResult.emit(Result.success(result))
            } catch (e: Exception) {
                _deleteMenuResult.emit(Result.failure(e))
            }
        }
    }

    fun getAllMenu(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.getAllMenu(id)
                _allMenuResult.emit((result))
            } catch (e: Exception) {
                _allMenuResult.emit(null)
            }
        }
    }

    fun deleteRestaurants(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.deleteRestaurants(id)
                _deleteRestaurantsResult.emit(Result.success(result))
            } catch (e: Exception) {
                _deleteRestaurantsResult.emit(Result.failure(e))
            }
        }
    }

    fun getOrder(id: String) {
        viewModelScope.launch {
            try {
                val result = repo.getAllOrders(id)
                _orderResult.emit(Result.success(result))
            } catch (e: Exception) {
                _orderResult.emit(Result.failure(e))
            }
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.deleteOrder(id)
                _deleteOrderResult.emit(Result.success(result))
            } catch (e: Exception) {
                _deleteOrderResult.emit(Result.failure(e))
            }
        }
    }
}