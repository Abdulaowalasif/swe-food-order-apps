package com.example.foodorderapps.user.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderapps.R
import com.example.foodorderapps.common.utils.Utils.Companion.HOME_FRAGMENT
import com.example.foodorderapps.common.utils.Utils.Companion.PROFILE_FRAGMENT
import com.example.foodorderapps.databinding.ActivityHomeScreenBinding
import com.example.foodorderapps.user.adapters.SearchAdapter
import com.example.foodorderapps.user.fragments.CartFragment
import com.example.foodorderapps.user.fragments.HomeScreenFragment
import com.example.foodorderapps.user.fragments.ProfileFragment
import com.example.foodorderapps.user.viewModels.AuthViewModel
import com.example.foodorderapps.user.viewModels.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private val binding: ActivityHomeScreenBinding by lazy {
        ActivityHomeScreenBinding.inflate(layoutInflater)
    }
    private val auth: AuthViewModel by viewModels()
    private val dataViewModel: DataViewModel by viewModels()
    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadFragment(HomeScreenFragment(), HOME_FRAGMENT)

        binding.searchRecycleView.layoutManager = GridLayoutManager(this, 2)
        binding.searchRecycleView.adapter = adapter

        auth.getCurrentUserInfo()
        setupHeaderView()
        setupDrawerToggle()
        setupNavigationListener()
        setupSearchAction()


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_lay)

                if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                    binding.drawer.closeDrawers()
                } else {
                    if (currentFragment !is HomeScreenFragment) {
                        supportFragmentManager.popBackStack()
                    } else {
                        finish()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun setupHeaderView() {
        val headerView = binding.navView.getHeaderView(0)
        val hProfile = headerView.findViewById<ImageView>(R.id.nav_profile)
        val hName = headerView.findViewById<TextView>(R.id.nav_name)
        val hEmail = headerView.findViewById<TextView>(R.id.nav_email)

        auth.userInfo.observe(this) { info ->
            Glide.with(this).load(info.image).into(hProfile)
            hName.text = info.username
            hEmail.text = info.email
            Glide.with(this).load(info.image).into(binding.toggleButton)
        }
    }

    private fun setupDrawerToggle() {
        binding.toggleButton.setOnClickListener {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupNavigationListener() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu_profile -> {
                    loadFragment(ProfileFragment(), PROFILE_FRAGMENT)
                }

                R.id.nav_menu_cart -> {
                    loadFragment(CartFragment(), "Cart")
                }

                R.id.nav_menu_share -> {
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_menu_logout -> {
                    auth.logout()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }

                R.id.nav_menu_setting -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                }
            }
            binding.drawer.closeDrawers()
            true
        }
    }

    private fun setupSearchAction() {
        val searchHandler = Handler(Looper.getMainLooper())
        val searchRunnable = Runnable {
            val query = binding.searchView.editText.text.toString().trim()
            if (query.isNotBlank()) {
                binding.searchBar.setText(query)
                dataViewModel.searchItem(query)

                lifecycleScope.launch {
                    dataViewModel.searchList.collect { list ->
                        adapter.submitList(list)
                        adapter.notifyDataSetChanged()
                    }
                }
            } else {
                clearSearch()  // Clear search if the query is blank
            }
        }

        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Remove any pending search Runnable to avoid multiple calls
                searchHandler.removeCallbacks(searchRunnable)
            }

            override fun afterTextChanged(s: Editable?) {
                // Post the search Runnable with a delay (e.g., 300ms)
                searchHandler.postDelayed(searchRunnable, 300)
            }
        })


        binding.searchView.editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                clearSearch()
            }
        }
    }

    private fun clearSearch() {
        binding.searchBar.setText("")
        lifecycleScope.launch {
            dataViewModel.clearSearch()
            adapter.submitList(emptyList())
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String?) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_lay)
        if (currentFragment?.javaClass != fragment.javaClass) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_lay, fragment, tag)

            if (tag == HOME_FRAGMENT) {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            } else {
                fragmentTransaction.addToBackStack(tag)
            }

            fragmentTransaction.commit()
        }
    }

}
