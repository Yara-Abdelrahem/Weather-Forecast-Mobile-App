package com.example.weathery.View

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import com.example.weathery.Model.AlertItem
import com.example.weathery.R
import com.example.weathery.ViewModel.AlarmViewModel
import java.util.Calendar

class SelectTimeFragment : Fragment() {
    private lateinit var btn_set_alert: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_set_alert=view.findViewById(R.id.btn_set_alert)


        val viewModel = AlarmViewModel(requireContext())

        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val btnSetAlarm = view.findViewById<Button>(R.id.btn_set_alert)

        timePicker.setIs24HourView(true)

        btn_set_alert.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val alarmData = AlertItem(time=calendar.timeInMillis, msg = "Wake up!")
            viewModel.setAlarm(alarmData)
            Toast.makeText(requireContext(), "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()
            (activity as? AlertActivity)?.finsh_set_alert()
            parentFragmentManager.popBackStack()
        }

    }

}