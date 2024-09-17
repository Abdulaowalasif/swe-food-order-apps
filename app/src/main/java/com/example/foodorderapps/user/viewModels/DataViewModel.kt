package com.example.foodorderapps.user.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.user.repositories.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {

    private val _restaurants = MutableStateFlow<List<Restaurants>?>(null)
    val restaurants: StateFlow<List<Restaurants>?> get() = _restaurants

    private val _menuList = MutableStateFlow<List<MenuList>?>(null)
    val menuList: StateFlow<List<MenuList>?> get() = _menuList

    private val _allMenuList = MutableStateFlow<List<MenuList>?>(null)
    val allMenuList: StateFlow<List<MenuList>?> get() = _allMenuList

    private val _searchList = MutableStateFlow<List<MenuList>?>(null)
    val searchList: StateFlow<List<MenuList>?> get() = _searchList

    private val _orderCreationStatus = MutableStateFlow<OrderList?>(null)
    val orderCreationStatus: StateFlow<OrderList?> get() = _orderCreationStatus

    private val _orderDeletionStatus = MutableStateFlow(false)
    val orderDeletionStatus: StateFlow<Boolean> get() = _orderDeletionStatus

    fun fetchAllRestaurants() {
        viewModelScope.launch {
            try {
                val result = userRepo.getAllRestaurants()
                _restaurants.emit(result)
            } catch (e: Exception) {
                _restaurants.value = null
            }
        }
    }

    fun fetchMenuById(id: String) {
        viewModelScope.launch {
            try {
                val result = userRepo.getAllMenuById(id)
                _menuList.emit(result)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun fetchAllMenu() {
        viewModelScope.launch {
            try {
                val result = userRepo.getAllMenu()
                _allMenuList.emit(result)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun searchItem(name: String) {
        viewModelScope.launch {
            try {
                val result = userRepo.searchItem(name)
                _searchList.emit(result)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun createOrder(orderList: OrderList) {
        viewModelScope.launch {
            try {
                val res = userRepo.createOrder(orderList)
                _orderCreationStatus.emit(res)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch {
            try {
                val res = userRepo.deleteOrder(id)
                _orderDeletionStatus.emit(res)
            } catch (e: Exception) {
                false
            }
        }
    }
}
