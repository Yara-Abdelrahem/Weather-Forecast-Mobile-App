package com.example.weathery.Favorite

import com.example.weathery.Favorite.Model.FavoriteCity

// in com.example.weathery.View
interface IFavClickListener {
     fun onNameCityClick(city: FavoriteCity)
     fun onDeleteFavCityClick(city: FavoriteCity)
}