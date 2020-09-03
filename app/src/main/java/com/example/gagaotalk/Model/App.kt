package com.example.gagaotalk.Model

import android.app.Application

class App : Application() {

    companion object {

        lateinit var prefs : SharedPref
        var friendDTOList : ArrayList<FriendDTO> = arrayListOf()
    }
    /* prefs라는 이름의 MySharedPreferences 하나만 생성할 수 있도록 설정. */

    override fun onCreate() {

        prefs = SharedPref(applicationContext)
        super.onCreate()
    }
}