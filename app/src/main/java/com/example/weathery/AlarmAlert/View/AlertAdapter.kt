package com.example.weathery.AlarmAlert.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.AlarmAlert.ViewModel.AlarmViewModel
import com.example.weathery.databinding.ItemAlertBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertAdapter(
    private var alerts: MutableList<AlertItem> = mutableListOf(),
    private val viewModel: AlarmViewModel,
    private val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    class AlertViewHolder(val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alerts[position]
        with(holder.binding) {
            timeRange.text = android.text.format.DateFormat.getTimeFormat(root.context).format(alert.time)
            messageText.text = alert.msg
            stopButton.setOnClickListener {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.cancelScheduledAlarm(root.context, alert)
                        viewModel.deleteAlertById(alert.id) // This triggers LiveData update
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (alerts.isNotEmpty()) {
            return alerts.size
        }else{
            return 0
        }
    }

    fun setAlerts(newAlerts: MutableList<AlertItem>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }
}
