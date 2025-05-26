package com.example.weathery.View.ui.Alerts

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weathery.Model.AlertItem
import com.example.weathery.R
import com.example.weathery.View.INavFragmaent
import com.example.weathery.View.ui.FavoriteCity.ShowFavoriteFragment
import com.example.yourapp.viewmodel.AlarmViewModel
import java.util.Calendar

class SelectTimeFragment : Fragment() {
    private lateinit var btnSetAlert: Button
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSetAlert = view.findViewById(R.id.btn_set_alert)
        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)

        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        btnSetAlert.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (!alarmManager.canScheduleExactAlarms()) {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Please allow exact alarms", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            val hour = timePicker.hour
            val minute = timePicker.minute

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val alarmData = AlertItem(time = calendar.timeInMillis, msg = "Wake up!")
            alarmViewModel.setAlarm(alarmData)
            Toast.makeText(requireContext(), "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()

            val activity = requireActivity() as INavFragmaent

            activity.navigateTo(ShowAlertFragment(),false)
        }
    }
}