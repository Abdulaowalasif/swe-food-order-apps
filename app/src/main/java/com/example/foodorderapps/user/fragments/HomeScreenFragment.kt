package com.example.foodorderapps.user.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodorderapps.R
import com.example.foodorderapps.databinding.FragmentHomeScreenBinding
import com.example.foodorderapps.user.adapters.RestaurantAdapter
import com.example.foodorderapps.user.adapters.TopItemAdapter
import com.example.foodorderapps.user.models.Restaurant
import com.example.foodorderapps.user.models.TopItems
import com.example.foodorderapps.user.viewModels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    companion object {
        fun newInstance() = HomeScreenFragment()
    }

    private val viewModel: HomeScreenViewModel by viewModels()
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var topItemAdapter: TopItemAdapter
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = arrayListOf(
            SlideModel(R.drawable.a),
            SlideModel(R.drawable.b),
            SlideModel(R.drawable.c),
            SlideModel(R.drawable.d),
            SlideModel(R.drawable.a),
            SlideModel(R.drawable.b),
            SlideModel(R.drawable.c),
            SlideModel(R.drawable.d)
        )

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setSlideAnimation(AnimationTypes.CUBE_OUT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {

            }

            override fun onItemSelected(position: Int) {

            }
        })

        val items = arrayListOf(
            TopItems(image = R.drawable.b, "b"),
            TopItems(image = R.drawable.c, "c"),
            TopItems(image = R.drawable.d, "d"),
            TopItems(image = R.drawable.a, "a"),
            TopItems(image = R.drawable.b, "b"),
            TopItems(image = R.drawable.c, "c"),
            TopItems(image = R.drawable.d, "d"),
            TopItems(image = R.drawable.a, "a"),
        )
        topItemAdapter = TopItemAdapter(items)
        binding.topItem.adapter = topItemAdapter

        val restaurantList = arrayListOf(
            Restaurant(R.drawable.a, "Restaurant a"),
            Restaurant(R.drawable.b, "Restaurant b"),
            Restaurant(R.drawable.c, "Restaurant c"),
            Restaurant(R.drawable.d, "Restaurant d"),
            Restaurant(R.drawable.a, "Restaurant a"),
            Restaurant(R.drawable.b, "Restaurant b"),
            Restaurant(R.drawable.c, "Restaurant c"),
            Restaurant(R.drawable.d, "Restaurant d"),
            Restaurant(R.drawable.a, "Restaurant a"),
        )
        restaurantAdapter = RestaurantAdapter(restaurantList)
        binding.restaurantList.adapter = restaurantAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
