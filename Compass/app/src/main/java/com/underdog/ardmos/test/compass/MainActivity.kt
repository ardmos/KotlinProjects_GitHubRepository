package com.underdog.ardmos.test.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.round

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensor_manager : SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    var angle : Double = 0.0
    var direction : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1
        sensor_manager = getSystemService(SENSOR_SERVICE) as SensorManager
        // 2
        sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensor_manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
        // 3
        sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensor_manager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        TODO("Not yet implemented")
        // 1
        if (p0 == null) {
            return
        }
        // 2
        if (p0.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // 3
            System.arraycopy(p0.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (p0.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(p0.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientationAngles()

        val angleWithDirection = "$angle  $direction"
        val direction_textView = findViewById<TextView>(R.id.direction_textView)
        direction_textView.text = angleWithDirection
        // 2
        val compass_imageView = findViewById<ImageView>(R.id.compass_imageView)
        compass_imageView.rotation = angle.toFloat() * -1

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

    fun updateOrientationAngles() {
        // 1
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        // 2
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
        // 3
        val degrees = (Math.toDegrees(orientation.get(0).toDouble()) + 360.0) % 360.0
        // 4
        angle = round(degrees * 100) / 100

        direction = getDirection(degrees)
    }

    private fun getDirection(angle: Double): String {
        var direction = ""

        if (angle >= 350 || angle <= 10)
            direction = "N"
        if (angle < 350 && angle > 280)
            direction = "NW"
        if (angle <= 280 && angle > 260)
            direction = "W"
        if (angle <= 260 && angle > 190)
            direction = "SW"
        if (angle <= 190 && angle > 170)
            direction = "S"
        if (angle <= 170 && angle > 100)
            direction = "SE"
        if (angle <= 100 && angle > 80)
            direction = "E"
        if (angle <= 80 && angle > 10)
            direction = "NE"

        return direction
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensor_manager.unregisterListener(this)
    }
}


