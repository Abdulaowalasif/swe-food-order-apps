package com.example.foodorderapps.user.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderapps.R
import com.example.foodorderapps.common.utils.Utils.Companion.HOME_FRAGMENT
import com.example.foodorderapps.common.utils.Utils.Companion.PROFILE_FRAGMENT
import com.example.foodorderapps.databinding.ActivityHomeScreenBinding
import com.example.foodorderapps.user.adapters.SearchAdapter
import com.example.foodorderapps.user.fragments.HomeScreenFragment
import com.example.foodorderapps.user.fragments.ProfileFragment
import com.example.foodorderapps.user.viewModels.AuthViewModel
import com.example.foodorderapps.user.viewModels.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.Future

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private val binding: ActivityHomeScreenBinding by lazy {
        ActivityHomeScreenBinding.inflate(layoutInflater)
    }
    private val auth: AuthViewModel by viewModels()
    private val dataViewmodel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadFragment(HomeScreenFragment(), HOME_FRAGMENT)
        auth.getCurrentUserInfo()
        supportFragmentManager.addOnBackStackChangedListener {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            if (backStackEntryCount > 0) {
                val lastFragmentTag =
                    supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1).name
                updateToolbarTitle(lastFragmentTag)
            } else {
                supportActionBar?.title = HOME_FRAGMENT
            }
        }

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

        binding.toggleButton.setOnClickListener {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.openDrawer(GravityCompat.START)
            }

        }

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu_profile -> {
                    supportActionBar?.title = "profile"
                    binding.drawer.closeDrawers()
                    loadFragment(ProfileFragment(), PROFILE_FRAGMENT)
                }

                R.id.nav_menu_share -> {
                    binding.drawer.closeDrawers()
                    Toast.makeText(this, "share", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_menu_logout -> {
                    auth.logout()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_menu_search -> {
                    binding.drawer.closeDrawers()
                    loadFragment(HomeScreenFragment(), HOME_FRAGMENT)
                }

                R.id.nav_menu_setting -> {
                    binding.drawer.closeDrawers()
                    Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.searchView.clearFocus()
                binding.searchView.onActionViewCollapsed()
                dataViewmodel.clearSearch()
                binding.frameLay.visibility = VISIBLE
                binding.searchRecycleView.visibility = GONE
            } else {
                binding.frameLay.visibility = GONE
                binding.searchRecycleView.visibility = VISIBLE
            }
        }

        binding.main.setOnClickListener {
            binding.searchView.clearFocus()
            binding.searchView.onActionViewCollapsed()
            dataViewmodel.clearSearch()
            binding.frameLay.visibility = VISIBLE
            binding.searchRecycleView.visibility = GONE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        dataViewmodel.searchItem(newText)
                        search()
                    }, 1000)

                }
                return true
            }
        })
    }

    private fun search() {
        binding.searchRecycleView.layoutManager = GridLayoutManager(this, 2)
        val adapter = SearchAdapter()
        lifecycleScope.launch {
            dataViewmodel.searchList.collect { list ->
                adapter.submitList(list)
                binding.searchRecycleView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

    }

    private fun loadFragment(fragment: Fragment, tag: String? = null) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_lay, fragment, tag)
        val currentFragment = fragmentManager.findFragmentById(R.id.frame_lay)
        if (tag == HOME_FRAGMENT) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else if (currentFragment?.tag != tag) {
            fragmentTransaction.addToBackStack(tag)
        }

        fragmentTransaction.commit()
        updateToolbarTitle(tag)
    }

    private fun updateToolbarTitle(tag: String?) {
        when (tag) {
            PROFILE_FRAGMENT -> binding.title.text = PROFILE_FRAGMENT
            else -> binding.title.text = HOME_FRAGMENT
        }
    }
}