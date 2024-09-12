package com.example.foodorderapps.admin.repositories

import android.net.Uri
import android.util.Log
import com.example.foodorderapps.common.api.ApiInterface
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.models.OrderList
import com.example.foodorderapps.common.models.Restaurants
import com.example.foodorderapps.common.models.Admin
import com.example.foodorderapps.common.utils.Utils.Companion.ADMIN
import com.example.foodorderapps.common.utils.Utils.Companion.PROFILE
import com.example.foodorderapps.common.utils.Utils.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AdminRepositories @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage,
    private val apiInterface: ApiInterface
) {

    suspend fun signInAdmin(email: String, password: String): Result<Unit> {
        return try {
            val adminExists = checkAdminExists(email)
            if (adminExists) {
                signInWithEmailAndPassword(email, password)
            } else {
                Result.failure(Exception("User does not exist"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun checkAdminExists(email: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val reference = database.getReference(ADMIN)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val adminExists = snapshot.children.any {
                        val adminData = it.getValue(Admin::class.java)
                        adminData?.email == email
                    }
                    continuation.resume(adminExists)
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

    suspend fun signUpAdmin(
        email: String,
        password: String,
        username: String,
        image: String
    ): Result<Unit> {
        return try {
            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password).await()

            // Ensure UID is available
            val uid = auth.uid ?: throw Exception("UID is null")

            // Reference to upload image
            val adminRef = storage.getReference(ADMIN).child(uid).child(PROFILE.toString())
            val imageUri = Uri.parse(image)

            // Upload image and get download URL
            val downloadUrl = adminRef.putFile(imageUri).await()
                .let { adminRef.downloadUrl.await() }

            // Create Admin object and update database
            val admin = Admin(email, username, downloadUrl.toString())
            database.getReference(ADMIN).child(uid).setValue(admin).await()

            // Create restaurant through API
            try {
                withContext(Dispatchers.IO) {
                    apiInterface.createRestaurant(
                        Restaurants(
                            id = uid,
                            name = username,
                            image = downloadUrl.toString()
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("SignUpAdmin", "API call failed: ${e.message}")
                return Result.failure(e)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SignUpAdmin", "Operation failed: ${e.message}")
            Result.failure(e)
        }
    }


    fun getCurrentAdmin() = auth.currentUser

    suspend fun getCurrentAdminData(): Admin = suspendCancellableCoroutine { continuation ->
        auth.uid?.let { uid ->
            val adminRef: DatabaseReference = database.getReference(ADMIN).child(uid)
            adminRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val admin = snapshot.getValue(Admin::class.java)
                    if (admin != null) {
                        continuation.resume(admin)
                    } else {
                        continuation.resume(Admin())
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


    suspend fun addMenu(menuList: MenuList): MenuList? {
        return try {
            val response = apiInterface.addMenu(menuList)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteMenu(id: Long): Boolean {
        return try {
            val response = apiInterface.deleteMenu(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllMenu(id: String): List<MenuList>? {
        return try {
            val response = apiInterface.getMenuByRestaurantId(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllOrders(id: String): List<OrderList>? {
        return try {
            val response = apiInterface.getOrdersByRestaurantId(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteOrder(id: Long): Boolean {
        return try {
            val response = apiInterface.deleteOrder(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteRestaurants(id: String): Boolean {
        return try {
            val response = apiInterface.deleteRestaurant(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun checkAdmin(uid: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val reference = database.getReference(ADMIN)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val exists = snapshot.child(uid).exists()
                    continuation.resume(exists)
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

    suspend fun uid(): String = auth.uid.toString()
}
