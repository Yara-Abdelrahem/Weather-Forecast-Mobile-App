package com.example.weathery.Favorite

import com.example.weathery.Favorite.Model.FavoriteCity

interface IFavoriteCityDataSource {
    suspend fun getAllFavCities(): List<FavoriteCity>
    suspend fun insertFavCity(city: FavoriteCity)
    suspend fun deleteFavCity(city: FavoriteCity)
}