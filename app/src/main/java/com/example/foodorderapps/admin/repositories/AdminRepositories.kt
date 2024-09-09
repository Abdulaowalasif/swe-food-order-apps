package com.example.foodorderapps.admin.repositories

import android.net.Uri
import com.example.foodorderapps.admin.models.Admins
import com.example.foodorderapps.utils.Utils.Companion.ADMIN
import com.example.foodorderapps.utils.Utils.Companion.PROFILE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AdminRepositories @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) {
    suspend fun signInAdmin(email: String, password: String): Result<Unit> {
        return try {
            val adminExists = checkAdminExists(email)
            if (adminExists) {
                auth.signInWithEmailAndPassword(email, password).await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Admin does not exist"))
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
                        val adminData = it.getValue(Admins::class.java)
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


    suspend fun signUpAdmin(
        email: String,
        password: String,
        restaurantName: String,
        image: String
    ): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.uid?.let { uid ->
                val adminRef = storage.getReference(ADMIN).child(uid).child(PROFILE.toString())
                val imageUri = Uri.parse(image)
                adminRef.putFile(imageUri)
                    .addOnSuccessListener {
                        adminRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            val admin = Admins(email, restaurantName, downloadUrl.toString())
                            database.getReference(ADMIN).child(uid)
                                .setValue(admin)
                        }
                    }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentAdmin() = auth.currentUser

    suspend fun getCurrentAdminData(): Admins = suspendCancellableCoroutine { continuation ->
        auth.uid?.let { uid ->
            val adminRef: DatabaseReference = database.getReference(ADMIN).child(uid)
            adminRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val admin = snapshot.getValue(Admins::class.java)
                    if (admin != null) {
                        continuation.resume(admin)
                    } else {
                        continuation.resume(Admins()) // Return a default or empty Users object if data is null
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
}