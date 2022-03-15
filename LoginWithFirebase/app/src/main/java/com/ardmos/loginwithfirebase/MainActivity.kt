package com.ardmos.loginwithfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ardmos.loginwithfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)


    }
}