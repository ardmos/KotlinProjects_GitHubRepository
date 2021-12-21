package com.haeddoo.level

import android.animation.ObjectAnimator
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
        //sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    }

    override fun onResume() {
        super.onResume()

        sensor?.also{
            accel->sensor_manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensor_manager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 == null) return

        //vbinding.star.setPadding(0, p0.values[1].toInt()*10, p0.values[0].toInt()*10, 0 )
        //Main
        ObjectAnimator.ofFloat(vbinding.orangeMain, "translationX", p0.values[0]*50f*-1).apply {
            //duration = 2000
            start()
        }
        ObjectAnimator.ofFloat(vbinding.orangeMain, "translationY", p0.values[1]*50f).apply {
            //duration = 2000
            start()
        }

        //Left
        ObjectAnimator.ofFloat(vbinding.orangeLeft, "translationY", p0.values[1]*50f).apply {
            //duration = 2000
            start()
        }
        //Bottom
        ObjectAnimator.ofFloat(vbinding.orangeBottom, "translationX", p0.values[0]*50f*-1).apply {
            //duration = 2000
            start()
        }

        //이동이 뻑뻑함
        //vbinding.orangeBottom.translationX = p0.values[0]*50f*-1


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}