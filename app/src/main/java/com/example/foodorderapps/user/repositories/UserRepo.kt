package com.example.foodorderapps.user.repositories

import android.net.Uri
import com.example.foodorderapps.common.api.ApiInterface
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.common.models.Admin
import com.example.foodorderapps.common.models.Cart
import com.example.foodorderapps.common.utils.Utils.Companion.PROFILE
import com.example.foodorderapps.common.utils.Utils.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class UserRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val database: FirebaseDatabase,
    private val apiInterface: ApiInterface
) {
    suspend fun signInUser(email: String, password: String): Result<Unit> {
        return try {
            val userExists = checkUserExists(email)
            if (userExists) {
                signInWithEmailAndPassword(email, password)
            } else {
                Result.failure(Exception("User does not exist"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkUserExists(email: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val reference = database.getReference(USERS)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userExists = snapshot.children.any {
                        val userData = it.getValue(Admin::class.java)
                        userData?.email == email
                    }
                    continuation.resume(userExists)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            }
            reference.addValueEventListener(listener)

            continuation.invokeOnCancellation {
                reference.removeEventListener(listener)
            }
        }
    }

    private suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(Unit))
                    } else {
                        continuation.resume(
                            Result.failure(
                                task.exception ?: Exception("Sign-in failed")
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(exception))
                }
        }
    }

    suspend fun signUpUser(
        email: String,
        password: String,
        username: String,
        image: String
    ): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.uid?.let { uid ->
                val usrRef = storage.getReference(USERS).child(uid).child(PROFILE.toString())
                val imageUri = Uri.parse(image)
                usrRef.putFile(imageUri)
                    .addOnSuccessListener {
                        usrRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            val user = Admin(email, username, downloadUrl.toString())
                            database.getReference(USERS).child(uid)
                                .setValue(user)
                        }
                    }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser() = auth.currentUser

    suspend fun getCurrentUserData(): Admin = suspendCancellableCoroutine { continuation ->
        auth.uid?.let { uid ->
            val userRef: DatabaseReference = database.getReference(USERS).child(uid)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usr = snapshot.getValue(Admin::class.java)
                    if (usr != null) {
                        continuation.resume(usr)
                    } else {
                        continuation.resume(Admin()) // Return a default or empty Users object if data is null
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        } ?: run {
            // Handle case where uid is null
            continuation.resumeWithException(IllegalStateException("User ID is null"))
        }
    }

    fun logOut() = auth.signOut()

    suspend fun getAllRestaurants(): List<Restaurants>? {
        return try {
            val response = apiInterface.getAllRestaurants()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            // Optionally log the exception here
            null
        }
    }

    suspend fun getAllMenuById(id: String): List<MenuList>? {
        return try {
            val response = apiInterface.getMenuByRestaurantId(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            // Optionally log the exception here
            null
        }
    }

    suspend fun getMenuByMenuId(id: Long): MenuList? {
        return try {
            val response = apiInterface.getMenuByMenuId(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            // Optionally log the exception here
            null
        }
    }

    suspend fun getAllMenu(): List<MenuList>? {
        return try {
            val response = apiInterface.getAllMenus()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            // Optionally log the exception here
            null
        }
    }

    suspend fun createOrder(orderList: OrderList): OrderList? {
        return try {
            val response = apiInterface.createOrder(orderList)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            // Optionally log the exception here
            null
        }
    }

    suspend fun deleteOrder(id: Long): Boolean {
        return try {
            val response = apiInterface.deleteOrder(id)
            response.isSuccessful
        } catch (e: Exception) {
            // Optionally log the exception here
            false
        }
    }

    suspend fun searchItem(name: String): List<MenuList>? {
        return try {
            val response = apiInterface.searchMenuByName(name)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            // Optionally log the exception here
            null
        }
    }

    suspend fun addCart(cart: Cart): Cart? {
        return try {
            val response = apiInterface.addCart(cart)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCart(id: String): List<Cart>? {
        return try {
            val response = apiInterface.getAllCart(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteCart(id: Long): Boolean {
        return try {
            val response = apiInterface.deleteCart(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

}