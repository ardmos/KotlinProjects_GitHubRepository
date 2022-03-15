package com.alzio.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alzio.simplenoteapp.databinding.ActivityMain2Binding
import com.alzio.simplenoteapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    private val vbinding by lazy { ActivityMain2Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        var auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()

        vbinding.buttonSubmit.setOnClickListener {
            // 목표: 현재 로그인 되어있는 사용자의 데이터를 입력받은 데이터로 수정

            // 1. 현재 로그인 되어있는 사용자의 데이터 베이스에 접근
            db.collection("users").document(auth.currentUser!!.uid)
            // 2. 데이터베이스의 데이터를 입력받은 데이터로 수정
                // 필드 업데이트
                //.update("name",vbinding.editTextPersonName.text.toString())
                /*.update(mapOf(
                    // age
                    "age" to vbinding.editTextAge.text.toString(),
                    // name
                    "name" to vbinding.editTextPersonName.text.toString()
                ))*/

                // 필드 삭제
                //.update("Name", FieldValue.delete())
                .update(mapOf(
                    // one
                    "one" to FieldValue.delete(),
                    // two
                    "two" to vbinding.editTextPersonName.text.toString()
                ))



        }
    }
}