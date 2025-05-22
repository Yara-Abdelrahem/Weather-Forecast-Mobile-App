//package com.example.weathery.Network
//
//import android.util.Log
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class ApiClientRetrofit {
//
//     val apiService: WeatherInterface
//
//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://dummyjson.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        apiService = retrofit.create(WeatherInterface::class.java)
//    }
//
//    fun getProducts(networkCallback: NetworkCallback) {
//        val call = apiService.getProducts()
//        call.enqueue(object : Callback<ProductObj> {
//            override fun onResponse(call: Call<WeatherObj>, response: Response<WeatherObj>) {
//                if (response.isSuccessful) {
//                    val weather_obj = response.body()?.products ?: emptyList()
//                    Log.d("ApiClientRetrofit", "Fetched Weather data ")
//                    networkCallback.onSuccessResult()
//                } else {
//                    Log.e("ApiClientRetrofit", "Failed with code ${response.code()}")
//                    networkCallback.onFailure("Failed with response code: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<WeatherObj>, t: Throwable) {
//                Log.e(TAG, "onFailure: ${t.message}", t)
//                networkCallback.onFailure(t.message ?: "Unknown error")
//            }
//        })
//    }
//
//    companion object {
//        private const val TAG = "ApiClientRetrofit"
//    }
//}