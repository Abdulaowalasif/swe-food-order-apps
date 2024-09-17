package com.example.foodorderapps.user.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodorderapps.databinding.FragmentHomeScreenBinding
import com.example.foodorderapps.user.adapters.RestaurantAdapter
import com.example.foodorderapps.user.adapters.TopItemAdapter
import com.example.foodorderapps.user.viewModels.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    companion object {
        fun newInstance() = HomeScreenFragment()
    }

    private val dataViewModel: DataViewModel by viewModels()
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private var topItemAdapter: TopItemAdapter = TopItemAdapter()
    private var restaurantAdapter: RestaurantAdapter = RestaurantAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewModel.fetchAllRestaurants()
        dataViewModel.fetchAllMenu()

        lifecycleScope.launch {
            dataViewModel.allMenuList.collect { list ->
                // Safely handle null list and limit to the first 5 items
                val limitedList = list?.take(5) ?: emptyList()

                // Create a list of SlideModel from the limited list
                val imageList = limitedList.map { item -> SlideModel(item.image) }

                // Configure the image slider
                binding.imageSlider.setImageList(imageList)
                binding.imageSlider.setSlideAnimation(AnimationTypes.CUBE_OUT)
            }

        }

        lifecycleScope.launch {
            dataViewModel.allMenuList.collect { list ->

                list?.let {
                    val limitedList = if (list.size > 7) list.take(7) else list
                    binding.topItem.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    topItemAdapter.submitList(limitedList)
                    binding.topItem.adapter = topItemAdapter
                    topItemAdapter.notifyDataSetChanged()

                }
            }
        }

        lifecycleScope.launch {
            dataViewModel.restaurants.collect { list ->
                binding.restaurantList.layoutManager = GridLayoutManager(context, 3)
                restaurantAdapter.submitList(list)
                binding.restaurantList.adapter = restaurantAdapter
                restaurantAdapter.notifyDataSetChanged()

            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
