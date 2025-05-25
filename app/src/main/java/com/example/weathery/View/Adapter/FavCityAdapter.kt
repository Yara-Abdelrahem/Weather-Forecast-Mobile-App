package com.example.weathery.View.Adapter

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weathery.Model.FavoriteCity

class FavCityAdapter(val items: List<FavoriteCity>)
    : RecyclerView.Adapter<FavCityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_list_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = items[position].city_name
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val tv = item.findViewById<TextView>(R.id.text1)

    }
}