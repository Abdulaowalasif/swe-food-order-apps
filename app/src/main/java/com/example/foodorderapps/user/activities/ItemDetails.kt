package com.example.foodorderapps.user.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodorderapps.common.models.Cart
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import com.example.foodorderapps.common.utils.Utils.Companion.ITEM_DETAILS
import com.example.foodorderapps.databinding.ItemDetailsBinding
import com.example.foodorderapps.user.viewModels.AuthViewModel
import com.example.foodorderapps.user.viewModels.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class ItemDetails : AppCompatActivity() {

    private val binding: ItemDetailsBinding by lazy {
        ItemDetailsBinding.inflate(layoutInflater)
    }

    private var quantitytxt: Int = 1
    private var totalPrice = 0.0
    private var basePrice = 0.0

    private val data: DataViewModel by viewModels()
    private val auth: AuthViewModel by viewModels()

    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val item = intent.getSerializableExtra(ITEM_DETAILS) as? MenuList
        uid = auth.getCurrentUser()?.uid.toString()

        binding.apply {
            item?.let {
                itemPrice.text = it.price.toString()
                itemTitle.text = it.name
                basePrice = it.price
                totalPrice = it.price
                Glide.with(this@ItemDetails).load(it.image).into(binding.image)
            }

            increment.setOnClickListener {
                if (quantitytxt > 0) {
                    quantitytxt++
                    totalPrice = basePrice * quantitytxt
                    itemPrice.text = String.format("%.2f", totalPrice)
                    quantity.text = quantitytxt.toString()
                }
            }
            decrement.setOnClickListener {
                if (quantitytxt > 1) {
                    quantitytxt--
                    totalPrice = basePrice * quantitytxt
                    itemPrice.text = String.format("%.2f", totalPrice)
                    quantity.text = quantitytxt.toString()
                }
            }

            order.setOnClickListener {
                binding.progressBar.visibility = VISIBLE
                binding.btnOrderSegment.visibility = GONE

                val list = item?.id?.let { menuId ->
                    OrderList(
                        restaurantId = item.restaurantId,
                        image = item.image,
                        menuId = menuId,
                        quantity = quantitytxt,
                        totalAmount = totalPrice,
                        location = address.text.toString(),
                        username = username.text.toString(),
                        itemName = item.name,
                        description = item.description,
                        phone = phone.text.toString()
                    )
                }

                if (list != null) {
                    data.createOrder(list)
                    Handler().postDelayed({
                        lifecycleScope.launch {
                            data.orderCreationStatus.collect { status ->

                                if (status.isSuccess) {
                                    binding.progressBar.visibility = GONE
                                    binding.btnOrderSegment.visibility = VISIBLE
                                } else {
                                    binding.progressBar.visibility = GONE
                                    binding.btnOrderSegment.visibility = VISIBLE

                                }
                            }
                        }
                    }, 1000)

                }
            }

            cart.setOnClickListener {
                val cartItem = item?.id?.let { it1 ->
                    Cart(
                        menuListId = it1,
                        quantity = quantitytxt,
                        userId = uid
                    )
                }
                cartItem?.let { it1 -> data.addCart(it1) }

//                data.getCart(uid)
//                data.allCart.value.map {
//                    it?.forEach { item->
//                        data.fetchMenuByMenuId(item.menuListId)
//                    }
//                }
//                data.menu.map {
//
//                }

            }
        }
    }
}