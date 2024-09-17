package com.example.foodorderapps.common.models

import java.io.Serializable

data class Restaurants(
    var id: String,
    var name: String,
    var image: String
) : Serializable