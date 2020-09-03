package com.example.gagaotalk

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gagaotalk.Model.*
import com.example.gagaotalk.network.RetrofitClient.client
import com.example.gagaotalk.network.ServiceApi
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSigninClient : GoogleSignInClient
    private lateinit var email: String
    var GOOGLE_LOGIN_CODE = 9001
    private var service: ServiceApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        service = client!!.create(ServiceApi::class.java)

        auth = FirebaseAuth.getInstance()

        // set shared_pref


        // google login
        google_sign_in_button.setOnClickListener {
            // First_Step
            googleLogin()
        }

        // sign in by email
        signin_signinbtn.setOnClickListener {
            attemptLogin()
        }
        // sign up by email
        signin_signupbtn.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("959026849644-fm4k88mu3ndcsgcm8jpkfkted0hbc0j8.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSigninClient = GoogleSignIn.getClient(this,gso)
    }

    override fun onStart(){
        super.onStart()


//        val user : FirebaseUser? = auth.currentUser
//        Log.e("로그인 확인", user!!.email)
//        moveMainPage(auth.currentUser)
    }

    private fun attemptLogin() {
        signin_email.setError(null)
        signin_password.setError(null)
        email = signin_email.getText().toString()
        val password: String = signin_password.getText().toString()
        var cancel = false
        var focusView: View? = null
        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            signin_password.setError("비밀번호를 입력해주세요.")
            focusView = signin_password
            cancel = true
        } else if (!isPasswordValid(password)) {
            signin_password.setError("6자 이상의 비밀번호를 입력해주세요.")
            focusView = signin_password
            cancel = true
        }
        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            signin_email.setError("이메일을 입력해주세요.")
            focusView = signin_email
            cancel = true
        } else if (!isEmailValid(email)) {
            signin_email.setError("@를 포함한 유효한 이메일을 입력해주세요.")
            focusView = signin_email
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            startLogin(LoginDTO(email, password))
        }
    }

    private fun startLogin(data: LoginDTO) {
        service?.userLogin(data)?.enqueue(object : Callback<LoginResponseDTO?> {
            override fun onResponse(
                call: Call<LoginResponseDTO?>?,
                response: Response<LoginResponseDTO?>
            ) {
                val result: LoginResponseDTO = response.body()!!
                Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                Log.e("로그인 정보 확인 by email",result.toString())
                if(result.code==200)
                {
                    // 나중에 jwt 토큰으로 변경해줄것. 현재는 그냥 아이디를 저장하였음.
                    App.prefs.myEmail = email
                    App.prefs.myName = result.userName

                    // 리사이클러뷰 데이터 본인정보 삽입
                    App.friendDTOList.add(FriendDTO(0,App.prefs.myName,"test"))
                    loadFreinds(FriendsDTO(App.prefs.myEmail))
                }
            }

            override fun onFailure(call: Call<LoginResponseDTO?>?, t: Throwable) {
                Log.e("로그인 에러 발생", t.message!!)
            }
        })
    }


    private fun loadFreinds(data: FriendsDTO) {
        service?.loadFriend(data)?.enqueue(object : Callback<FriendsResponseDTO?> {
            override fun onResponse(
                call: Call<FriendsResponseDTO?>?,
                response: Response<FriendsResponseDTO?>
            ) {
                val result: FriendsResponseDTO = response.body()!!
                Log.e("친구목록 불러오기 확인",result.toString())
                if(result.code==200)
                {
                    // 이메일을 기준으로 db에서 이름, 상메 불러오기
                    loadStatus(UserStatusDTO(result.friendList[0]))

                }
            }

            override fun onFailure(call: Call<FriendsResponseDTO?>?, t: Throwable) {
                Log.e("로그인 에러 발생", t.message!!)
            }
        })
    }

    private fun loadStatus(data: UserStatusDTO) {
        service?.loadStatus(data)?.enqueue(object : Callback<UserStatusResponseDTO?> {
            override fun onResponse(
                call: Call<UserStatusResponseDTO?>?,
                response: Response<UserStatusResponseDTO?>
            ) {
                val result: UserStatusResponseDTO = response.body()!!
                Log.e("상태 불러오기 확인",result.toString())
                if(result.code==200)
                {
                    // 이메일을 기준으로 db에서 이름, 메세지 받아와야해.
                    App.friendDTOList.add(FriendDTO(1,result.userName,result.userMsg))
                    moveMainPage_()

                }
            }

            override fun onFailure(call: Call<UserStatusResponseDTO?>?, t: Throwable) {
                Toast.makeText(applicationContext, "로그인 에러 발생", Toast.LENGTH_SHORT).show()
                Log.e("로그인 에러 발생", t.message)
            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }


    fun googleLogin(){
        val signInIntent = googleSigninClient?.signInIntent
        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)!!
            if(result.isSuccess){
                var account = result.signInAccount
                //Second_Step
                firebaseAuthWithGoogle(account)
            }
            
        }
    }

    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    //Login
                    moveMainPage(task.result?.user)
                }else{
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()
                    //show the error message
                }
            }
    }

    fun moveMainPage(user: FirebaseUser?){
        if(user != null){
            Log.e("로그인 정보 확인",user.email)
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    fun moveMainPage_(){
        var intent = Intent(applicationContext,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }




}
