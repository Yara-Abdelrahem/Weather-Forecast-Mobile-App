package com.example.weathery.View.ui.FavoriteCity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weathery.Model.FavoriteCity
import com.example.weathery.Model.FavoriteCityRepositry
import com.example.weathery.Model.LocalFavorityCityDatasource
import com.example.weathery.R
import com.example.weathery.ViewModel.FavoriteCityViewModel
import com.example.weathery.WeatherDatabase
import com.example.weathery.databinding.FragmentFavCityBinding
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavCityBinding? = null
    private val binding get() = _binding!!

    private lateinit var favCityViewModel: FavoriteCityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favCityDao = WeatherDatabase.getDatabase(requireContext()).weatherDao()

        favCityViewModel = FavoriteCityViewModel(
            FavoriteCityRepositry(
                LocalFavorityCityDatasource(favCityDao)
            )
        )
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShowFavoriteFragment())
            .commit()

//        if (savedInstanceState == null) {
//            childFragmentManager.beginTransaction()
//                .add(R.id.fragment_container, ShowFavoriteFragment())
//                .commit()
//        }
    }

//    fun openMapPicker() {
//        childFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, SelectFavoriteLocationFragment())
//            .addToBackStack(null)
//            .commit()
//    }
//
//    fun onLocationPicked(lat: Double, lon: Double, city: String) {
//        childFragmentManager.popBackStack()
//        val label = "$city ($lat, $lon)"
//        lifecycleScope.launch {
//            favCityViewModel.insertFavCity(FavoriteCity(city_name = city, city_lat = lat, city_lon = lon))
//        }
//        Toast.makeText(requireContext(), "Location added to favorites: $label", Toast.LENGTH_SHORT).show()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
