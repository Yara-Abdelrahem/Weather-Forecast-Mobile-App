package com.example.weathery.AlarmAlert

import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weathery.R

class AlarmActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val message = intent.getStringExtra("message") ?: "Alarm!"
        val id = intent.getIntExtra("id", 0)
        // Show when locked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        findViewById<TextView>(R.id.tvAlarmMessage).text = message

        // Start alarm sound
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer?.start()

        findViewById<Button>(R.id.btnDismiss).setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}