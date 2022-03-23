package com.ardmos.login_with_firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ardmos.login_with_firebase.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.Holder>() {
    val listData = mutableListOf<Memo>()

    class Holder(val vbinding: ItemRecyclerBinding): RecyclerView.ViewHolder(vbinding.root){
        fun setMemo( memo: Memo){   // fun 이름 첫글자 소문자?, 파라미터에 var or val 안써도 됨?
            vbinding.message.text = memo.message

            var sdf = SimpleDateFormat("yyyy/MM//dd")   // SimpleDateFormat이란?
            var formattedData = sdf.format(memo.timestamp)
            vbinding.date.text = formattedData
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val vbinding = ItemRecyclerBinding.inflate(LayoutInflater.from(p0.context), p0, false)  // 얘네에 대해서 분석.
        return Holder(vbinding)
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        val memo = listData.get(p1)
        p0.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

