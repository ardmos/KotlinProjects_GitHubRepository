package com.ardmos.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ardmos.login_with_firebase.databinding.ActivityJoinBinding
import com.ardmos.login_with_firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class JoinActivity : AppCompatActivity() {
    private var TAG = ""
    val vbinding by lazy { ActivityJoinBinding.inflate(layoutInflater)}
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        TAG = intent.getStringExtra("TAG").toString()

        // 스피너에 입력할 데이터 - 탄생년도
        var data = mutableListOf("- 선택하세요 -")
        for(year in 1950..2022){
            data.add(year.toString())
        }
        Log.d(TAG, data.size.toString())
        // 데이터와 스피너를 연결해줄 어댑터 만들기 - 컨텍스트, 기본제공 아이템 레이아웃, 데이터
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        // 어댑터를 스피너에 연결
        vbinding.spinner.adapter = adapter
        // 어댑터 입력 처리
        var selected_item : String = "_"
        vbinding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            //p2: Int가 선택된 아이템의 포지션값.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected_item = data.get(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // button create account
        vbinding.buttonCreateAccount.setOnClickListener {
            // email
            val id = vbinding.inputIdJoinpage.text.toString()
            // password
            val pwd = vbinding.inputPasswordJoinpage.text.toString()
            // birth year
            Log.d(TAG, "birth year: ${selected_item}")

            auth.createUserWithEmailAndPassword(id, pwd).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success, user: ${auth.currentUser}")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }

            // move activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}