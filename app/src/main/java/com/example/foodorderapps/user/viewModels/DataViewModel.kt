package com.example.foodorderapps.user.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.common.models.Cart
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

    private val _menu = MutableStateFlow<MenuList?>(null)
    val menu: StateFlow<MenuList?> get() = _menu

    private val _allMenuList = MutableStateFlow<List<MenuList>?>(null)
    val allMenuList: StateFlow<List<MenuList>?> get() = _allMenuList

    private val _searchList = MutableStateFlow<List<MenuList>?>(null)
    val searchList: StateFlow<List<MenuList>?> get() = _searchList

    private val _orderCreationStatus = MutableStateFlow<Result<OrderList?>>(Result.success(null))
    val orderCreationStatus: StateFlow<Result<OrderList?>> get() = _orderCreationStatus

    private val _orderDeletionStatus = MutableStateFlow(false)
    val orderDeletionStatus: StateFlow<Boolean> get() = _orderDeletionStatus

    private val _addCart = MutableStateFlow<Result<Cart?>>(Result.success(null))
    val addCart: StateFlow<Result<Cart?>> get() = _addCart

    private val _allCart = MutableStateFlow<Result<List<Cart>?>>(Result.success(null))
    val allCart: StateFlow<Result<List<Cart>?>> get() = _allCart

    private val _deleteCart = MutableStateFlow(false)
    val deleteCart: StateFlow<Boolean> get() = _deleteCart

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
                _menuList.emit(null)
            }
        }
    }

    fun fetchMenuByMenuId(id: Long) {
        viewModelScope.launch {
            try {
                val result = userRepo.getMenuByMenuId(id)
                _menu.emit(result)
            } catch (e: Exception) {
                _menu.emit(null)
            }
        }
    }

    fun fetchAllMenu() {
        viewModelScope.launch {
            try {
                val result = userRepo.getAllMenu()
                _allMenuList.emit(result)
            } catch (e: Exception) {
                _allMenuList.emit(null)
            }
        }
    }

    fun searchItem(name: String) {
        viewModelScope.launch {
            try {
                val result = userRepo.searchItem(name)
                _searchList.emit(result)
            } catch (e: Exception) {
                _searchList.emit(null)
            }
        }
    }

    fun createOrder(orderList: OrderList) {
        viewModelScope.launch {
            try {
                val res = userRepo.createOrder(orderList)
                _orderCreationStatus.emit(Result.success(res))
            } catch (e: Exception) {
                _orderCreationStatus.emit(Result.failure(e))
            }
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch {
            try {
                val res = userRepo.deleteOrder(id)
                _orderDeletionStatus.emit(res)
            } catch (e: Exception) {
                _orderDeletionStatus.emit(false)
            }
        }
    }

    fun clearSearch() {
        viewModelScope.launch {
            _searchList.emit(emptyList())
        }
    }

    fun addCart(cart: Cart) {
        viewModelScope.launch {
            try {
                val res = userRepo.addCart(cart)
                _addCart.emit(Result.success(res))
            } catch (e: Exception) {
                _addCart.emit(Result.failure(e))
            }
        }
    }

    fun getCart(id: String) {
        viewModelScope.launch {
            try {
                val res = userRepo.getCart(id)
                _allCart.emit(Result.success(res))
            } catch (e: Exception) {
                _allCart.emit(Result.failure(e))
            }
        }
    }

    fun deleteCart(id: Long) {
        viewModelScope.launch {
            try {
                val res = userRepo.deleteCart(id)
                _deleteCart.emit(res)
            } catch (e: Exception) {
                _deleteCart.emit(false)
            }
        }
    }
}
