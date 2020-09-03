package com.example.gagaotalk.Model

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context){

    val prefs_filename = "prefs"
    val key_myEmail = "email"
    val key_myName = "name"
    val prefs: SharedPreferences = context.getSharedPreferences(prefs_filename, 0)

    var myEmail: String
        get() = prefs.getString(key_myEmail, "")!!
        set(value) = prefs.edit().putString(key_myEmail, value).apply()

    var myName: String
        get() = prefs.getString(key_myName, "")!!
        set(value) = prefs.edit().putString(key_myName, value).apply()
    /* get/set 함수 임의 설정. get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장 */
}