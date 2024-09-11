package com.example.foodorderapps.common.models

data class OrderList(
    val id: Long = 0,
    val restaurantId: String,
    val menuId: Long,
    val image: String,
    val quantity: Int,
    val totalAmount: Double
)
