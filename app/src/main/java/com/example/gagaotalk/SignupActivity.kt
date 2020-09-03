package com.example.gagaotalk

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gagaotalk.Model.JoinDTO
import com.example.gagaotalk.Model.JoinResponseDTO
import com.example.gagaotalk.network.RetrofitClient.client
import com.example.gagaotalk.network.ServiceApi
import kotlinx.android.synthetic.main.activity_signup.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        service = client!!.create(ServiceApi::class.java)

        Log.e("service???",service.toString())

        join_button.setOnClickListener {
            attemptJoin()
        }
    }

    private fun attemptJoin() {
        join_email.setError(null)
        join_name.setError(null)
        join_password.setError(null)
        val name: String = join_name.getText().toString()
        val email: String = join_email.getText().toString()
        val password: String = join_password.getText().toString()
        var cancel = false
        var focusView: View? = null
        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            join_password.setError("비밀번호를 입력해주세요.")
            focusView = join_password
            cancel = true
        } else if (!isPasswordValid(password)) {
            join_password.setError("6자 이상의 비밀번호를 입력해주세요.")
            focusView = join_password
            cancel = true
        }
        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            join_email.setError("이메일을 입력해주세요.")
            focusView = join_email
            cancel = true
        } else if (!isEmailValid(email)) {
            join_email.setError("@를 포함한 유효한 이메일을 입력해주세요.")
            focusView = join_email
            cancel = true
        }
        // 이름의 유효성 검사
        if (name.isEmpty()) {
            join_name.setError("이름을 입력해주세요.")
            focusView = join_name
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            startJoin(JoinDTO(name, email, password))
            showProgress(true)
        }
    }

    private fun startJoin(data: JoinDTO) {
        service?.userJoin(data)?.enqueue(object : Callback<JoinResponseDTO?> {
            override fun onResponse(
                call: Call<JoinResponseDTO?>?,
                response: Response<JoinResponseDTO?>
            ) {
                val result: JoinResponseDTO = response.body()!!
                val t1 = Toast.makeText(applicationContext,result.message,Toast.LENGTH_SHORT)
                t1.show()
                showProgress(false)
                if (result.code === 200) {
                    finish()
                }
            }

            override fun onFailure(call: Call<JoinResponseDTO?>, t: Throwable) {
                Log.e("회원가입 에러 발생", t.message);
                showProgress(false);
            }

        })
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
    private fun showProgress(show: Boolean) {
        join_progress.setVisibility(if (show) View.VISIBLE else View.GONE)
    }

}
