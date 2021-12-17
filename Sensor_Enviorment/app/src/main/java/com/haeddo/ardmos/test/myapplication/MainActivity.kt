package com.haeddo.ardmos.test.myapplication

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView


class MainActivity : AppCompatActivity(), SensorEventListener  {


    private lateinit var sensorManager: SensorManager
    private lateinit var textview0 : TextView
    private lateinit var textview1 : TextView
    private lateinit var textview2 : TextView
    private lateinit var textview3 : TextView
    private var pressure: Sensor? = null
    private var light: Sensor? = null
    private var temperature: Sensor? = null
    private var humidity: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textview0 = findViewById<TextView>(R.id.textView0)
        textview1 = findViewById<TextView>(R.id.textView1)
        textview2 = findViewById<TextView>(R.id.textView2)
        textview3 = findViewById<TextView>(R.id.textView3)

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)


        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            // Success! There's a temperature sensor.
            temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            Log.d("mylog :", "Success! There's a temperature sensor.")
        } else {
            // Failure! No temperature sensor.
            Log.d("mylog :", "Failure! No temperature sensor.")
            textview2.text = "No temperature sensor."
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        }else{

            textview3.text = "No humidity sensor."
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        //val millibarsOfPressure = event.values[0]
        //val lxOfLight = event.values[0]
        //val cOfTemperature = event.values[0]

        //Log.d("millibarsOfPressure ", millibarsOfPressure.toString())
        //Log.d("SensorEvent size ", event.values.size.toString())

        //var textview0 = findViewById<TextView>(R.id.textView0)
        //var textview1 = findViewById<TextView>(R.id.textView1)
        //var textview2 = findViewById<TextView>(R.id.textView2)

        //textview0.text = millibarsOfPressure.toString()
        //textview1.text = lxOfLight.toString()
        //textview2.text = cOfTemperature.toString()

        // Do something with this sensor data.
        val sensor : Sensor = event.sensor

        if(sensor.getType() == Sensor.TYPE_PRESSURE){
            val millibarsOfPressure = event.values[0]

            textview0.text = "pressure : " + millibarsOfPressure.toString()
        }
        else if(sensor.getType() == Sensor.TYPE_LIGHT){
            val lxOfLight = event.values[0]

            textview1.text = "light : " + lxOfLight.toString()
        }
        else if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            val cOfTemperature = event.values[0]
            Log.d("Temperature check : ", cOfTemperature.toString())

            textview2.text = cOfTemperature.toString()
        }
        else if(sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
            val humidity = event.values[0]
            textview3.text = humidity.toString()
        }

    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}


