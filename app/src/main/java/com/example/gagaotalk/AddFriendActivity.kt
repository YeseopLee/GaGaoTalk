package com.example.gagaotalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.gagaotalk.Model.*
import com.example.gagaotalk.network.RetrofitClient.client
import com.example.gagaotalk.network.ServiceApi
import kotlinx.android.synthetic.main.activity_add_friend.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        service = client!!.create(ServiceApi::class.java)

        addfriend_backbtn.setOnClickListener{
            finish()
        }

        addfriend_confirm.setOnClickListener {
            //TODO
            val email = addfriend_edittext.text.toString()
            var cancel = false
            var focusView: View? = null

            if(email.isEmpty()){
                addfriend_edittext.error = "이메일을 입력하세요"
                cancel = true
            }
            else if(!isEmailValid(email)){
                addfriend_edittext.error = "올바른 메일 주소를 입력하세요."
                cancel = true
            }
//            else if(!isEmailRegistered(email)){
//                addfriend_edittext.error = "등록되지 않은 사용자입니다."
//            }

            if(cancel){
                focusView?.requestFocus()
            }
            else{
                Log.e("친구추가 확인", App.prefs.myEmail+","+email)
                addFriend(AddFriendDTO(App.prefs.myEmail,email))
            }
        }
    }

    private fun addFriend(data: AddFriendDTO) {
        service?.addFriend(data)?.enqueue(object : Callback<AddFriendResponseDTO?> {
            override fun onResponse(
                call: Call<AddFriendResponseDTO?>?,
                response: Response<AddFriendResponseDTO?>
            ) {
                val result: AddFriendResponseDTO = response.body()!!
                val t1 = Toast.makeText(applicationContext,result.message, Toast.LENGTH_SHORT)
                t1.show()
                if (result.code === 200) {
                    finish()
                }
            }

            override fun onFailure(call: Call<AddFriendResponseDTO?>, t: Throwable) {
                Log.e("친구추가 에러 발생", t.message);
            }

        })
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isEmailRegistered(email: String): Boolean {
        return true
    }
}
