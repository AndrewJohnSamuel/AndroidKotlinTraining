package com.andrew.testservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.Nullable

class MyBackgroundService : Service() {

    private var player: MediaPlayer? = null
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer.create(
            this, Settings.System.DEFAULT_RINGTONE_URI
        )
        player!!.isLooping = true
        player!!.start()
        return START_STICKY
    }

//    @Nullable
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
//        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
    }
}