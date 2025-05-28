package com.example.weathery.View.ui.FavoriteCity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.Favorite.Model.FavoriteCity
import com.example.weathery.Favorite.Model.FavoriteCityRepositry
import com.example.weathery.Favorite.Model.LocalFavorityCityDatasource
import com.example.weathery.R
import com.example.weathery.Favorite.Adapter.FavCityAdapter
import com.example.weathery.View.IFavClickListener
import com.example.weathery.View.INavFragmaent
import com.example.weathery.Favorite.ViewModel.FavoriteCityViewModel
import com.example.weathery.WeatherDatabase
import kotlinx.coroutines.launch

class ShowFavoriteFragment : Fragment()  , IFavClickListener {
    private lateinit var viewModel : FavoriteCityViewModel

    private lateinit var rvFavorites: RecyclerView
    private lateinit var btn_add_favorite : Button
    private val favoriteLocations = mutableListOf<String>()
    private lateinit var adapter: FavCityAdapter
    private var selected_fragment = SelectFavoriteLocationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_show_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorites = view.findViewById(R.id.rvFavorites)
        btn_add_favorite = view.findViewById(R.id.btnAddToFavorites)
        rvFavorites.layoutManager = LinearLayoutManager(requireContext())

        val dao = WeatherDatabase.Companion.getDatabase(requireContext()).weatherDao()
        val repo = FavoriteCityRepositry(LocalFavorityCityDatasource(dao))
         viewModel = FavoriteCityViewModel(repo)

        lifecycleScope.launch {
            val favList = viewModel.getAllFavCity()
            adapter = FavCityAdapter(viewModel.Fav_city_ret, this@ShowFavoriteFragment)
            rvFavorites.adapter = adapter

        }
        btn_add_favorite.setOnClickListener {
            val activity = requireActivity() as INavFragmaent

            activity.navigateTo(SelectFavoriteLocationFragment(),false)
//                    childFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, SelectFavoriteLocationFragment())
//            .addToBackStack(null)
//            .commit()


//            requireActivity().supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragment_container, SelectFavoriteLocationFragment())
//                .addToBackStack(null)
//                .commit()
        }
    }

    override fun onNameCityClick(city: FavoriteCity) {
        //show city details
    }

    override fun onDeleteFavCityClick(city: FavoriteCity) {
        //delete city from favorite
        lifecycleScope.launch {
            viewModel.deletFavCity(city)
        }
    }
}