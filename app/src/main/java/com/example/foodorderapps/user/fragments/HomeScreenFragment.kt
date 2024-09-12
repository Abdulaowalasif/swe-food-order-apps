package com.example.foodorderapps.user.fragments

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodorderapps.R
import com.example.foodorderapps.databinding.FragmentHomeScreenBinding
import com.example.foodorderapps.user.adapters.RestaurantAdapter
import com.example.foodorderapps.user.adapters.TopItemAdapter
import com.example.foodorderapps.user.viewModels.DataViewmodel
import com.example.foodorderapps.user.viewModels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    companion object {
        fun newInstance() = HomeScreenFragment()
    }

    private val dataViewModel: DataViewmodel by viewModels()
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var topItemAdapter: TopItemAdapter
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataViewModel.fetchAllRestaurants()
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.menuList.observe(viewLifecycleOwner) { list ->
            list?.forEach() { item ->
                val imageList = arrayListOf(SlideModel(item.image))
                binding.imageSlider.setImageList(imageList.subList(0, 5))
                binding.imageSlider.setSlideAnimation(AnimationTypes.CUBE_OUT)
            }
        }

        dataViewModel.menuList.observe(viewLifecycleOwner) { list ->
            binding.topItem.layoutManager = LinearLayoutManager(context)
            if (::topItemAdapter.isInitialized) {
                topItemAdapter.upateData(list)
            } else {
                topItemAdapter = list?.let { TopItemAdapter(it) }!!
                binding.topItem.adapter = topItemAdapter
            }
        }

        dataViewModel.restaurants.observe(viewLifecycleOwner) { list ->
            binding.restaurantList.layoutManager = GridLayoutManager(context, 3)
            if (::restaurantAdapter.isInitialized) {
                restaurantAdapter.updateData(list)
            } else {
                restaurantAdapter = RestaurantAdapter(list)
                binding.restaurantList.adapter = restaurantAdapter
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
