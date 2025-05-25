package com.example.weathery.View.Adapter

import com.example.weathery.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.Model.FavoriteCity
import com.example.weathery.View.IFavClickListener

class FavCityAdapter(val items: MutableLiveData<List<FavoriteCity>>, val listener: IFavClickListener)
    : RecyclerView.Adapter<FavCityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fav_city_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_city_name.text = items.value.get(position).city_name

        holder.tv_city_name.setOnClickListener {
            Log.i("Fav city click" , "${items.value.get(position).city_name} clicked")
            listener.onNameCityClick(items.value.get(position))
        }

        holder.btn_delet_fav_city.setOnClickListener {
            items.value?.let {
                if (position >= 0 && position < it.size) {
                    listener.onDeleteFavCityClick(it[position])

                    val mutableList = it.toMutableList()
                    mutableList.removeAt(position)
                    items.value = mutableList
                    notifyItemRemoved(position)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return items.value.size
    }
    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val tv_city_name = item.findViewById<TextView>(R.id.tv_city_name)
        val btn_delet_fav_city = item.findViewById<Button>(R.id.btn_delete_fav_city)

    }
}