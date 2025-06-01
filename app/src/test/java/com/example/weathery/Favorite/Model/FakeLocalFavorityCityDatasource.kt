package com.example.weathery.Favorite.Model

import com.example.weathery.Favorite.*
import com.example.weathery.Model.WeatherDao
import com.example.weathery.Favorite.IFavoriteCityDataSource
import org.mockito.Mockito.mock

class FakeFavoriteCityDataSource : IFavoriteCityDataSource {
    private val cities = mutableListOf<FavoriteCity>()

    override suspend fun getAllFavCities(): List<FavoriteCity> = cities

    override suspend fun insertFavCity(city: FavoriteCity) {
        cities.add(city)
    }

    override suspend fun deleteFavCity(city: FavoriteCity) {
        cities.remove(city)
    }
}

