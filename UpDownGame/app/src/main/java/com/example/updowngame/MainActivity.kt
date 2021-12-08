package com.example.updowngame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.updowngame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var str : String ?= null

    val vbinding by lazy {  ActivityMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        //mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vbinding.root)


        var isStarted : Boolean = false
        var ranNum : Int = 0


        vbinding.btnStart.setOnClickListener {
            // 2. 한 번 뽑힌 숫자는 변하지 않는다. (이미 숫자가 뽑혔는가? if문)
            if(isStarted){

            }
            else{
                // 1. 컴퓨터가 임의의 숫자를 뽑는다.
                ranNum = (1..100).random()
                //Log.d("랜덤숫자", ranNum.toString())
                val mytoast: Toast = Toast.makeText(this, "랜덤숫자: $ranNum", Toast.LENGTH_LONG)
                mytoast.show()
                isStarted = true
            }
        }

        vbinding.btnSubmit.setOnClickListener {
            val edittext1 : EditText = vbinding.editText1
            Log.d("에딧텍스트테스트", edittext1.text.toString())

            // 4. 정답 여부를 알려주는 TextView.
            if(ranNum.toString() == edittext1.text.toString()){
                val mytoast: Toast = Toast.makeText(this, "성공입니다! $ranNum", Toast.LENGTH_LONG)
                mytoast.show()
            }
            else{
                if (ranNum.toString().toInt() <= edittext1.text.toString().toInt()){
                    val mytoast: Toast = Toast.makeText(this, "업! $ranNum", Toast.LENGTH_LONG)
                    mytoast.show()
                }
                else if (ranNum.toString().toInt() >= edittext1.text.toString().toInt()){
                    val mytoast: Toast = Toast.makeText(this, "다운! $ranNum", Toast.LENGTH_LONG)
                    mytoast.show()
                }
            }
        }


        //Log.d("if문 테스트", "1")
        //if(1==2){
        //    Log.d("if문 테스트", "2")
        //}else{
        //    Log.d("if문 테스트", "3")
        //}
        //Log.d("if문 테스트", "4")

        ///
        //컴퓨터가 임의의 숫자를 생각해내면 사용자가 그 숫자를 맞추는 게임.

        /*
        val btnStart : Button = findViewById(R.id.btnStart)
        btnStart.setOnClickListener {
            // 2. 한 번 뽑힌 숫자는 변하지 않는다. (이미 숫자가 뽑혔는가? if문)
            if(isStarted){

            }
            else{
                // 1. 컴퓨터가 임의의 숫자를 뽑는다.
                ranNum = (1..100).random()
                //Log.d("랜덤숫자", ranNum.toString())
                val mytoast: Toast = Toast.makeText(this, "랜덤숫자: $ranNum", Toast.LENGTH_LONG)
                mytoast.show()
                isStarted = true
            }
        }
        */


        /*
        // 3. 사용자의 입력을 처리할 수 있는 공간 필요. (EditText, Button)
        val btnSubmit: Button = findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val edittext1 : EditText = findViewById(R.id.editText1)
            Log.d("에딧텍스트테스트", edittext1.text.toString())

            // 4. 정답 여부를 알려주는 TextView.
            if(ranNum.toString() == edittext1.text.toString()){
                val mytoast: Toast = Toast.makeText(this, "성공입니다! $ranNum", Toast.LENGTH_LONG)
                mytoast.show()
            }
            else{
                if (ranNum.toString().toInt() <= edittext1.text.toString().toInt()){
                    val mytoast: Toast = Toast.makeText(this, "업! $ranNum", Toast.LENGTH_LONG)
                    mytoast.show()
                }
                else if (ranNum.toString().toInt() >= edittext1.text.toString().toInt()){
                    val mytoast: Toast = Toast.makeText(this, "다운! $ranNum", Toast.LENGTH_LONG)
                    mytoast.show()
                }
            }
        }
        */


    }

    override fun onDestroy() {
        super.onDestroy()

    }
}