package com.example.gagaotalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        /*RecyclerView 데이터 전달 받음*/
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        txt_name.text = name

        btn_close.setOnClickListener{
            finish()
        }
        btn_chat.setOnClickListener {
            val iT = (Intent(this,ChatActivity::class.java))
            iT.putExtra("name",txt_name.text.toString())
            iT.putExtra("username",username)
            startActivity(iT)
        }
    }
}
