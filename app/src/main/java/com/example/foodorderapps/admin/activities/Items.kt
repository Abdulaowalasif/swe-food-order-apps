package com.example.foodorderapps.admin.activities

import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodorderapps.R
import com.example.foodorderapps.admin.viewmodels.AdminDataViewmodel
import com.example.foodorderapps.common.models.MenuList
import com.example.foodorderapps.common.utils.Utils.Companion.ITEM_REF
import com.example.foodorderapps.common.utils.Utils.Companion.PROFILE
import com.example.foodorderapps.common.utils.Utils.Companion.navigateToNext
import com.example.foodorderapps.databinding.ActivityItemsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class Items : AppCompatActivity() {
    private val binding: ActivityItemsBinding by lazy {
        ActivityItemsBinding.inflate(layoutInflater)
    }
    private lateinit var imagePicker: ActivityResultLauncher<String>
    private var imageUri = "no image uploaded."

    @Inject
    lateinit var storage: FirebaseStorage

    @Inject
    lateinit var auth: FirebaseAuth

    private val data: AdminDataViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            imagePicker = registerForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri ->
                if (uri != null) {
                    imageUri = uri.toString()
                    itemImage.setImageURI(uri)
                }
            }

            itemImage.setOnClickListener {
                imagePicker.launch("image/*")
            }
            add.setOnClickListener {
                adding.visibility = VISIBLE
                val name = name.text.toString()
                val price = price.text.toString()

                if (name.isNotEmpty() && price.isNotEmpty() && imageUri.isNotEmpty()) {
                    val itemsRef = storage.getReference(ITEM_REF).child(PROFILE.toString())

                    itemsRef.putFile(Uri.parse(imageUri)).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            itemsRef.downloadUrl.addOnCompleteListener { urlTask ->
                                if (urlTask.isSuccessful) {
                                    val downloadUrl = urlTask.result.toString()

                                    val menu = MenuList(
                                        name = name,
                                        price = price.toDouble(),
                                        image = downloadUrl,
                                        restaurantId = auth.uid.toString(),
                                        description = "this is description"
                                    )
                                    data.addMenu(menu)
                                    lifecycleScope.launch {
                                        data.menuResult.collect { res ->
                                            if (res.isSuccess) {
                                                adding.visibility = GONE
                                                navigateToNext(this@Items, AdminHome::class.java)
                                                finish()
                                            } else {
                                                adding.visibility = GONE
                                            }
                                        }
                                    }
                                    Toast.makeText(this@Items, "Menu Added", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(
                                        this@Items,
                                        "Failed to retrieve download URL",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(this@Items, "Upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@Items, "Please fill in all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }
}