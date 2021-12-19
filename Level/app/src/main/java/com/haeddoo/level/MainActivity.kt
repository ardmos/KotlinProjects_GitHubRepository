package com.haeddoo.level

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.haeddoo.level.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    val vbinding by lazy{ ActivityMainBinding.inflate(layoutInflater) }


    private lateinit var sensor_manager : SensorManager
    private var sensor : Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        sensor_manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    override fun onResume() {
        super.onResume()

        sensor?.also{
            rotation->sensor_manager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensor_manager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 == null) return

        vbinding.textViewX.text = "x: ${p0.values[0]}"
        vbinding.textViewY.text = "y: ${p0.values[1]}"
        vbinding.textViewZ.text = "z: ${p0.values[2]}"

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}