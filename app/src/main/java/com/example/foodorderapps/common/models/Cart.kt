package com.example.foodorderapps.common.models

data class Cart(
    var menuListId: Long,
    var quantity: Int,
    var userId: String
)