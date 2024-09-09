package com.example.foodorderapps.user.activities

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderapps.R
import com.example.foodorderapps.databinding.ActivityHomeScreenBinding
import com.example.foodorderapps.user.adapters.SearchAdapter
import com.example.foodorderapps.user.fragments.HomeScreenFragment
import com.example.foodorderapps.user.fragments.ProfileFragment
import com.example.foodorderapps.user.models.Search
import com.example.foodorderapps.utils.Utils.Companion.HOME_FRAGMENT
import com.example.foodorderapps.utils.Utils.Companion.PROFILE_FRAGMENT
import com.example.foodorderapps.user.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private val binding: ActivityHomeScreenBinding by lazy {
        ActivityHomeScreenBinding.inflate(layoutInflater)
    }
    private val auth: AuthViewModel by viewModels()
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        loadFragment(HomeScreenFragment(), HOME_FRAGMENT)

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

        auth.getCurrentUserInfo()
        auth.userInfo.observe(this) { info ->
            Glide.with(this).load(info.image).into(hProfile)
            hName.text = info.username
            hEmail.text = info.email
        }

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

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
            binding.frameLay.visibility = VISIBLE
            binding.searchRecycleView.visibility = GONE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let { search(it) }
                return true
            }
        })
    }

//    private fun getItems(query: String): List<Search> {
//        val listItems = ArrayList<Search>()
//        //hit the api and get all the items by name
//        return listItems
//    }
//
//    private fun search(query: String) {
//        val items = getItems(query)
//        val adapter = SearchAdapter(items)
//        binding.searchRecycleView.adapter = adapter
//        binding.searchRecycleView.layoutManager = GridLayoutManager(this, 2)
//    }

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
            PROFILE_FRAGMENT -> supportActionBar?.title = PROFILE_FRAGMENT
            else -> supportActionBar?.title = HOME_FRAGMENT
        }
    }
}
