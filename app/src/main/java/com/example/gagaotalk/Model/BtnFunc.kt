package com.example.gagaotalk.Model

import android.os.SystemClock
import android.view.View

// 버튼 중복클릭 방지 클릭 리스너
abstract class OnSingleClickListener : View.OnClickListener{

    private var MIN_CLICK_INTERVAL : Long = 600;
    private var LAST_CLICK_TIME : Long = 0;

    abstract fun onSingleClick(view: View)

    override fun onClick(v: View?) {
        var current_click_time = SystemClock.uptimeMillis()
        var possible_time = current_click_time - LAST_CLICK_TIME
        LAST_CLICK_TIME = current_click_time

        if (possible_time > MIN_CLICK_INTERVAL){
            onSingleClick(v!!)
        }
    }
}