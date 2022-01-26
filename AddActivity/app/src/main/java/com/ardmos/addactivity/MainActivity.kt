package com.ardmos.addactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ardmos.addactivity.databinding.ActivityMainBinding

//다른 액티비티를 여는 방법

class MainActivity : AppCompatActivity() {

    val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        hello("okey")

        /*  1. 그냥 액티비티 열기만 하기.
        vbinding.button.setOnClickListener {
            //1. intent 생성
            val intent = Intent(this, SubActivity::class.java)
            //2. startActivity
            startActivity(intent)
        }*/

        //  2. 액티비티 열면서 값 전달하기.
        vbinding.button.setOnClickListener {
            //1. intent 생성
            val intent = Intent(this, SubActivity::class.java)
            //2. putExtra 메서드를 사용해 인텐트에 값을 전달
            intent.putExtra("id_data", "myid")
            intent.putExtra("pwd_data", "abcdpwd")
            //3. startActivity
            startActivity(intent)
        }
/*
        vbinding.button.setOnClickListener {
            var user_data = ""

            //user_data.plus(vbinding.inputFirstname.text.toString())
            user_data += """
                Name
                ID
                PWD
            """.trimIndent()

            //Log.d("user_data", user_data)

            openFileOutput("user_data.txt", Context.MODE_PRIVATE).use {
                    stream -> stream.write(user_data.toByteArray())
            }
        }

        vbinding.button2.setOnClickListener {
            var contents = ""

            openFileInput("user_data.txt").bufferedReader().useLines { lines ->
                Log.d("log", "read1: " + contents)
                contents = lines.joinToString("\n")
                Log.d("log", "read2: " + contents)

            }

            var list = contents.split("\n")
            list.forEach {
                Log.d("log", it)
            }

        }*/
    }

    private fun hello(txt : String){
        Log.d("hello", txt)
    }
}