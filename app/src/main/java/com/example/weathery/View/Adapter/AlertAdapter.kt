package com.example.weathery.View

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.Model.AlertItem
import com.example.weathery.R
import java.text.SimpleDateFormat
import java.util.Locale

class AlertAdapter : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {
    private var alerts: List<AlertItem> = emptyList()

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeText: TextView = itemView.findViewById(R.id.time_range)
        val btn_delete: Button = itemView.findViewById(R.id.btn_delete_alert)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alerts[position]
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.timeText.text = sdf.format(alert.time)
        holder.btn_delete.setOnClickListener {
            Log.i("Alert Adapter" , "Alert deleted")
        }
    }

    override fun getItemCount(): Int = alerts.size

    fun submitList(newAlerts: List<AlertItem>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }
}