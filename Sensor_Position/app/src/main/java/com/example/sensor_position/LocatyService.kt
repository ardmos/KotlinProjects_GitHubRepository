package com.example.sensor_position

import android.app.Service
import android.content.Intent
import android.hardware.SensorManager
import android.os.IBinder

class LocatyService : Service() {
    private lateinit var sensorManager: SensorManager

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}