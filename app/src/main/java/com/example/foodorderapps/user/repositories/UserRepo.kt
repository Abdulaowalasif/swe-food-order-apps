package com.example.foodorderapps.user.repositories

import android.net.Uri
import com.example.foodorderapps.user.models.Users
import com.example.foodorderapps.utils.Utils.Companion.PROFILE
import com.example.foodorderapps.utils.Utils.Companion.USERS
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
    private val database: FirebaseDatabase
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

    private suspend fun checkUserExists(email: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val reference = database.getReference(USERS)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userExists = snapshot.children.any {
                        val userData = it.getValue(Users::class.java)
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
                            val user = Users(email, username, downloadUrl.toString())
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
    suspend fun getCurrentUserData(): Users = suspendCancellableCoroutine { continuation ->
        auth.uid?.let { uid ->
            val userRef: DatabaseReference = database.getReference(USERS).child(uid)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usr = snapshot.getValue(Users::class.java)
                    if (usr != null) {
                        continuation.resume(usr)
                    } else {
                        continuation.resume(Users()) // Return a default or empty Users object if data is null
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