package com.example.foodorderapps.common.models

import java.io.Serializable

data class OrderList(
    val id: Long?=0,
    val restaurantId: String,
    val menuId: Long,
    val image: String,
    val itemName:String,
    val quantity: Int,
    val totalAmount: Double,
    val location: String,
    val username: String,
    val phone: String,
    val description: String
) : Serializable
