package com.example.joinpage

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.joinpage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)


        //checkbox
        vbinding.checkBoxOne.setOnCheckedChangeListener { checkBox, isChecked ->
            //Log.d("checkbox_one", checkBox.text.toString() + ", " + isChecked.toString())
            if (isChecked) Log.d("checkbox_one", "checked true")
            else Log.d("checkbox_one", "checked false")
        }

        //radio button
        vbinding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.radioButton_android -> Log.d("radioGroup", vbinding.radioButtonAndroid.text.toString())
                R.id.radioButton_ios -> Log.d("radioGroup", vbinding.radioButtonIos.text.toString())
            }
        }


    }
}