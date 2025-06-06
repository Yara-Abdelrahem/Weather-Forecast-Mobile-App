package com.example.weathery.Favorite.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathery.Favorite.Model.FavoriteCity
import com.example.weathery.Favorite.Model.FavoriteCityRepositry

class FavoriteCityViewModel (var repo : FavoriteCityRepositry): ViewModel() {
    private var favcitylist : MutableLiveData<List<FavoriteCity>> = MutableLiveData<List<FavoriteCity>>()
    var Fav_city_ret = favcitylist

    suspend fun getAllFavCity() : List<FavoriteCity>{
        favcitylist.value = repo.getFavCities()
        return favcitylist.value
    }
    suspend fun insertFavCity(city: FavoriteCity){
        repo.insertFavCity(city)
    }
    suspend fun deletFavCity(city: FavoriteCity){
        repo.deleteFavCity(city)
    }
}