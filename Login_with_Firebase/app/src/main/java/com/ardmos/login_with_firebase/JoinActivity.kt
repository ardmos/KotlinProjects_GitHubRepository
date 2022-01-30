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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class JoinActivity : AppCompatActivity() {
    private var TAG = ""
    val vbinding by lazy { ActivityJoinBinding.inflate(layoutInflater)}
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)
        // get TAG data from LoginActivity
        TAG = intent.getStringExtra("TAG").toString()
        // Initialize Firebase Auth
        auth = Firebase.auth
        // Access a Cloud Firestore instance from your Activity
        db = Firebase.firestore


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
                //사용 안함
            }
        }

        // button create account
        vbinding.buttonCreateAccount.setOnClickListener {
            // email
            val id = vbinding.inputIdJoinpage.text.toString()
            // password
            val pwd = vbinding.inputPasswordJoinpage.text.toString()
            // birth year
            val birth_year = selected_item

            // Create a new user data
            val user = hashMapOf(
                "id" to id,
                "pwd" to pwd,
                "born" to birth_year
            )


            //create user at firebase auth & create database at firestore
            auth.createUserWithEmailAndPassword(id, pwd).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, create signed-in user's database at firestore and move activity
                    Log.d(TAG, "createUserWithEmail:success, user: ${auth.currentUser}")
                    //create signed-in user's database at firestore
                    //val user = auth.currentUser

                    //document의 ID가 자동으로 생성된다.
                    /*db.collection("users")
                        .add(user)*/

                    //원하는 document의 ID를 정해줄 수 있다. 아래에선 auth.currentUser!!.uid
                    db.collection("users")
                        .document(auth.currentUser!!.uid)
                        .set(user)



                    // move activity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }


        }

    }
}