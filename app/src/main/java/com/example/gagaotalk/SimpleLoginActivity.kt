package com.example.gagaotalk

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_simple_login.*

class SimpleLoginActivity : AppCompatActivity(){

    lateinit var prefs: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {

        prefs = PreferenceUtil(applicationContext)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_login)



        btn_leeyeseop.setOnClickListener {
            prefs.setString("username","이예섭")
            var iT = Intent(this,MainActivity::class.java)
            startActivity(iT)
        }

        btn_leejiseop.setOnClickListener {
            prefs.setString("username","이지섭")
            var iT = Intent(this,MainActivity::class.java)
            startActivity(iT)
        }
    }
}
