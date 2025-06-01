package com.example.weathery.Favorite.Model

import com.example.weathery.Favorite.IFavoriteCityDataSource

class FavoriteCityRepositry (val localDataSource:IFavoriteCityDataSource){
    companion object {
        @Volatile
        private var INSTANCE: FavoriteCityRepositry? = null
        fun getInstance(
            localDataSource: LocalFavorityCityDatasource
        ): FavoriteCityRepositry {
            return INSTANCE ?: synchronized(this) {
                val instance = FavoriteCityRepositry(localDataSource)
                INSTANCE = instance
                instance
            }
        }
    }
    suspend fun getFavCities(): List<FavoriteCity>{
        return localDataSource.getAllFavCities()
    }
    suspend fun insertFavCity(city: FavoriteCity){
        localDataSource.insertFavCity(city)
    }
    suspend fun deleteFavCity(city: FavoriteCity){
        localDataSource.deleteFavCity(city)
    }
}