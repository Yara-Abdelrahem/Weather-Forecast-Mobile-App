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
import android.widget.DatePicker
import android.widget.RadioGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.AlarmAlert.ViewModel.AlarmViewModel
import com.example.weathery.R
import com.example.weathery.View.INavFragmaent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class SelectTimeFragment : Fragment() {
    private lateinit var btnSetAlert: Button
//    private lateinit var btnStopAlarm: Button
    private lateinit var alarmViewModel: AlarmViewModel
    private var mediaPlayer: MediaPlayer? = null
    private var currentAlarmId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSetAlert = view.findViewById(R.id.btn_set_alert)
//        btnStopAlarm = view.findViewById(R.id.btn_stop_alarm)
        alarmViewModel = AlarmViewModel(requireContext())

        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val alertTypeGroup = view.findViewById<RadioGroup>(R.id.alertTypeGroup)
        timePicker.setIs24HourView(true)

        btnSetAlert.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (!alarmManager.canScheduleExactAlarms()) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = android.net.Uri.fromParts("package", requireContext().packageName, null)
                    }
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Please enable exact alarm permission in app settings", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            val hour = timePicker.hour
            val minute = timePicker.minute
            val year = datePicker.year
            val month = datePicker.month // 0-based (0-11)
            val day = datePicker.dayOfMonth

            val alertType = when (alertTypeGroup.checkedRadioButtonId) {
                R.id.radioNotification -> "notification"
                R.id.radioAlarm -> "alarm"
                else -> "notification"
            }

            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                }
            }

            val alarmData = AlertItem(time = calendar.timeInMillis, msg = "Check The Weather", type = alertType)
            lifecycleScope.launch {
                val alarmId = alarmViewModel.setAlarm(alarmData)
                currentAlarmId = alarmId
                val dateTimeString = "${day}/${month + 1}/${year} $hour:$minute"
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Alert set for $dateTimeString as $alertType", Toast.LENGTH_SHORT).show()
                }

                val activity = requireActivity() as INavFragmaent
                activity.navigateTo(AlertFragment(), false)
            }
        }

    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAlarm()
    }
}