package com.example.usecamera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.usecamera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.button.text = "HAAAA"
    }
}