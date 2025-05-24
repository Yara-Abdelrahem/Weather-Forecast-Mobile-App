package com.example.weathery.Model

import com.example.weathery.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class LocalFavorityCityDatasource(var citydao : WeatherDao){

    init {
    }
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