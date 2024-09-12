package com.example.foodorderapps.common.api

import android.view.MenuItem
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import com.example.foodorderapps.common.models.Restaurants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    //admin
    @POST("restaurants")
    suspend fun createRestaurant(@Body restaurantDTO: Restaurants): Response<Restaurants>

    //user
    @GET("restaurants")
    suspend fun getAllRestaurants(): Response<List<Restaurants>>

    //admin
    @DELETE("restaurants/{id}")
    suspend fun deleteRestaurant(@Path("id") id: String): Response<Void>

    //admin
    @POST("menus")
    suspend fun addMenu(@Body menuListDTO: MenuList): Response<MenuList>

    //admin
    @DELETE("menus/{id}")
    suspend fun deleteMenu(@Path("id") id: Long): Response<Void>


    @GET("menus")
    suspend fun getAllMenus(): Response<List<MenuList>>

    //admin,user
    @GET("menus/restaurant/{restaurantId}")
    suspend fun getMenuByRestaurantId(@Path("restaurantId") restaurantId: String): Response<List<MenuList>>

    //user
    @POST("orders")
    suspend fun createOrder(@Body orderList: OrderList): Response<OrderList>

    @GET("orders")
    suspend fun getAllOrders(): Response<List<OrderList>>

    //user,admin
    @DELETE("orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Long): Response<Void>

    //admin
    @GET("orders/restaurant/{restaurantId}")
    suspend fun getOrdersByRestaurantId(@Path("restaurantId") restaurantId: String): Response<List<OrderList>>

    @GET("menus/search")
    suspend fun searchMenuByName(@Query("name") name: String): Response<List<MenuList>>
}
