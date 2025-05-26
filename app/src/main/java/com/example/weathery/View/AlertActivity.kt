package com.example.yourapp.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weathery.R

class AlertActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        // Make the activity full-screen and show over lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        // Get the alarm message from the intent
        val message = intent.getStringExtra("message") ?: "Alarm!"
        findViewById<TextView>(R.id.tvAlarmMessage).text = message

        // Play the alarm sound
        mediaPlayer = MediaPlayer.create(this, R.raw.chicken_alarm2)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        // Dismiss button
        findViewById<Button>(R.id.btnDismiss).setOnClickListener {
            mediaPlayer.stop()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}