package com.example.weathery.View

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weathery.R
import com.example.weathery.Service.AlarmService
import com.example.weathery.View.ui.Alerts.ShowAlertFragment
import com.example.weathery.View.ui.Alerts.SelectTimeFragment

class AlarmAlertActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_alert)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frameLayoutAlarm, ShowAlertFragment())
                .commit()
        }

//        val message = intent.getStringExtra("message") ?: "Alarm!"
//        findViewById<TextView>(R.id.alarmMessage).text = message
//
//        mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
//        mediaPlayer.isLooping = true
//        mediaPlayer.start()
//
//        findViewById<Button>(R.id.dismissButton).setOnClickListener {
//            mediaPlayer.stop()
//            mediaPlayer.release()
//            finish()
//        }
    }


}