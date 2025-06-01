package com.example.weathery.Home

import androidx.fragment.app.Fragment

interface INavFragmaent {
       fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}