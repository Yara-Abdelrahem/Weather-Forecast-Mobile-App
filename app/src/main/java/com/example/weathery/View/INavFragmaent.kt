package com.example.weathery.View

import androidx.fragment.app.Fragment

interface INavFragmaent {
       fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}