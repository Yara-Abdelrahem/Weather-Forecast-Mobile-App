package com.example.weathery.Favorite.Model

import com.example.weathery.Model.WeatherDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalFavorityCityDatasource(var citydao : WeatherDao){
    suspend fun getAllFavCities(): List<FavoriteCity> {
        return citydao.getAllFavCities()
    }
    suspend fun insertFavCity(city: FavoriteCity){
        withContext(Dispatchers.IO) {
            citydao.insertFavorite(city)
        }
    }
    suspend fun deleteFavCity(city: FavoriteCity){
        withContext(Dispatchers.IO) {
            citydao.delete_fav_city(city)
        }
    }
}