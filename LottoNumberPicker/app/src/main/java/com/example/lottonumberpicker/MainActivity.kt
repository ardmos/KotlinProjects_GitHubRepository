package com.example.lottonumberpicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val textView : TextView = findViewById(R.id.textView2)
        val textView3 : TextView = findViewById(R.id.textView3)
        val editText : EditText = findViewById(R.id.editTextTextPersonName)
        val button : Button = findViewById(R.id.button)


        val ranNum: Int = (1..45).random()
        textView3.text = ranNum.toString()

        button.setOnClickListener {
            //val randomNumber = (0..2).random()

            //textView.text = randomNumber.toString()

            if (editText.text.toString().equals("")){
                val toast : Toast = Toast.makeText(this, "값이 없습니다.", Toast.LENGTH_LONG)
                toast.show()
            }else{

                if (editText.text.toString().toInt() == ranNum){
                    textView.text = "성공!"
                }
                else if(editText.text.toString().toInt() > ranNum){
                    textView.text = "Down!"
                }
                else{
                    textView.text = "Up!"
                }
            }





        }

    }
}