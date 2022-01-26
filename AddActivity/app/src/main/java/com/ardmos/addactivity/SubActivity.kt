package com.ardmos.addactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ardmos.addactivity.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {

    val vbinding by lazy { ActivitySubBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        Log.d("data from MainActivity", "${intent.getStringExtra("id_data")}")
        Log.d("data from MainActivity", "${intent.getStringExtra("pwd_data")}")
    }
}