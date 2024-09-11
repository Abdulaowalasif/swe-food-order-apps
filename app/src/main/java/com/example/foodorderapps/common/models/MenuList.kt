package com.example.foodorderapps.common.models

data class MenuList(
    var id: Long=0,
    var name: String,
    var price: Double,
    var image: String,
    var restaurantId: String
)