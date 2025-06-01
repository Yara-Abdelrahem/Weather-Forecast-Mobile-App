package com.example.weathery.Favorite.Model

import com.example.weathery.Model.WeatherDao
import com.example.weathery.Favorite.IFavoriteCityDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalFavorityCityDatasource(private val cityDao: WeatherDao) : IFavoriteCityDataSource {
    override suspend fun getAllFavCities(): List<FavoriteCity> {
        return cityDao.getAllFavCities()
    }

    override suspend fun insertFavCity(city: FavoriteCity) {
        cityDao.insertFavorite(city)
    }

    override suspend fun deleteFavCity(city: FavoriteCity) {
        cityDao.delete_fav_city(city)
    }
}
