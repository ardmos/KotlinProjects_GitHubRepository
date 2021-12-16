package com.haeddo.ardmos.test.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private lateinit var sensor_manager : SensorManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensor_manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val device_sensors : List<Sensor> = sensor_manager.getSensorList(Sensor.TYPE_ALL)

        for(s in device_sensors){
            Log.d("MySensors : ", s.name)
        }

    }
}