package com.alzio.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alzio.simplenoteapp.databinding.ActivityJoinBinding
import com.alzio.simplenoteapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JoinActivity : AppCompatActivity() {

    private val vbinding by lazy { ActivityJoinBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        var auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()

        vbinding.buttonSubmitJoinpage.setOnClickListener {
            auth.createUserWithEmailAndPassword(vbinding.editTextEmailIDJoinpage.text.toString(),vbinding.editTextPasswordJoinpage.text.toString())
                .addOnCompleteListener(this) {
                    if (it.isSuccessful){
                        // 계정 생성 성공
                        Toast.makeText(this, "계정 생성에 성공했습니다! 계정명: ${auth.currentUser}", Toast.LENGTH_LONG ).show()
                        Log.d("test", "계정 생성에 성공했습니다! 계정명: ${auth.currentUser}")

                        // 계정 생성에 성공한 사용자의 데이터베이스 도큐먼트를 생성하겠습니다
                        // 1. 데이터를 맵의 형태로 만듭니다.
                        //     age, name, message
                        var userData = mapOf(
                            "age" to vbinding.editTextAgeJoinpage.text.toString(),
                            "name" to vbinding.editTextNameJoinpage.text.toString(),
                            "message" to vbinding.editTextMessageJoinpage.text.toString()
                        )


                        // 2. 만들어준 데이터를 전송해줍니다.
                        db.collection("users").document(auth.currentUser!!.uid)
                            .set(userData)

                    }
                    else{
                        // 계정 생성 실패
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG ).show()
                        Log.d("test", it.exception.toString())
                    }
                }
        }
    }
}