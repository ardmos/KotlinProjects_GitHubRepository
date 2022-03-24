package com.alzio.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alzio.simplenoteapp.databinding.ActivityLoginBinding
import com.alzio.simplenoteapp.databinding.ActivityMainBinding
import com.alzio.simplenoteapp.databinding.ItemLayoutRecyclerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        var auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()


        // 리사이클러뷰
        // 1. 표현하고자 하는 데이터
        val data: MutableList<String> = mutableListOf()
        for (no in 1..100){
            data.add(" ${no}번 데이터")
        }



        // 3. 리사이클러뷰 자체의 레이아웃
        var recyclerViewAdapter = CustomAdapter()
        recyclerViewAdapter.listData = data

        vbinding.recyclerViewMain.adapter = recyclerViewAdapter
        vbinding.recyclerViewMain.layoutManager = LinearLayoutManager(this)






        // 회원 삭제
        /*vbinding.buttonDeleteMainpage.setOnClickListener {
            // 1. db 삭제
            db.collection("users").document(auth.currentUser!!.uid)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "DB 삭제에 성공했습니다!", Toast.LENGTH_LONG ).show()

                    // 2. auth 계정 삭제
                    auth.currentUser!!.delete()
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this, "계정 삭제에 성공했습니다!", Toast.LENGTH_LONG ).show()

                                onBackPressed()
                            }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "DB 삭제에 실패했습니다! ${it.message}", Toast.LENGTH_LONG ).show()
                }



        }*/

    }

    // 2. 리사이클러뷰 내의 아이템 레이아웃
    class CustomAdapter: RecyclerView.Adapter<CustomAdapter.Holder>(){
        var listData = mutableListOf<String>()

        // 표현하고자 하는 데이터와, 그 데이터를 담을 아이템 홀더(그릇)를 만들어서
        class Holder(val vbinding: ItemLayoutRecyclerBinding): RecyclerView.ViewHolder(vbinding.root){
            fun setData(message: String){
                vbinding.textMessageItem.text = message
            }
        }

        // 뷰홀더 준비
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
            // 1. 레이아웃 파일을 메모리에 올린다
            val vbinding = ItemLayoutRecyclerBinding.inflate(LayoutInflater.from(p0.context),p0,false)
            // 2. 홀더에 레이아웃을 넣어준다
            return Holder(vbinding)
        }

        // 뷰홀더를 직접 화면에 보여주기
        override fun onBindViewHolder(p0: Holder, p1: Int) {
            // 1. 데이터를 얻어온다
            val message = listData.get(p1)
            // 2. 얻어온 데이터를 홀더에 넣어준다
            p0.setData(message)
        }

        // 아이템 개수 카운트
        override fun getItemCount(): Int {
            return listData.size
        }
        // 그 둘을 합쳐줄겁니다.
    }
}