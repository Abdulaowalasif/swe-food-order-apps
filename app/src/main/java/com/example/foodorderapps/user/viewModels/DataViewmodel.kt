package com.example.foodorderapps.user.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.user.repositories.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewmodel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurants>?>()
    val restaurants: LiveData<List<Restaurants>?> get() = _restaurants

    private val _menuList = MutableLiveData<List<MenuList>?>()
    val menuList: LiveData<List<MenuList>?> get() = _menuList

    private val _orderCreationStatus = MutableLiveData<OrderList?>()
    val orderCreationStatus: LiveData<OrderList?> get() = _orderCreationStatus

    private val _orderDeletionStatus = MutableLiveData<Boolean>()
    val orderDeletionStatus: LiveData<Boolean> get() = _orderDeletionStatus

    fun fetchAllRestaurants() {
        viewModelScope.launch {
            _restaurants.value = try {
                userRepo.getAllRestaurants()
            } catch (e: Exception) {
                null
            }
        }
    }

    fun fetchMenuByRestaurantId(id: String) {
        viewModelScope.launch {
            _menuList.value = try {
                userRepo.getAllMenu(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun createOrder(orderList: OrderList) {
        viewModelScope.launch {
            _orderCreationStatus.value = try {
                userRepo.createOrder(orderList)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch {
            _orderDeletionStatus.value = try {
                userRepo.deleteOrder(id)
            } catch (e: Exception) {
                false
            }
        }
    }
}
