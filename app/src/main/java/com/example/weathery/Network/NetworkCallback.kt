package com.example.weathery.Network

interface NetworkCallback {
    fun onSuccessResult()
    fun onFailure(errorMsg: String)
}