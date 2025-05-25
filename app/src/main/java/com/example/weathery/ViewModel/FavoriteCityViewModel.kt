package com.example.weathery.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathery.Model.FavoriteCity
import com.example.weathery.Model.FavoriteCityRepositry
import kotlinx.coroutines.launch

class FavoriteCityViewModel (var repo : FavoriteCityRepositry): ViewModel() {
    var favcitylist : MutableLiveData<List<FavoriteCity>> = MutableLiveData<List<FavoriteCity>>()
    var Fav_city_ret = favcitylist

    init {

    }

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