package com.example.gagaotalk


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gagaotalk.Model.*
import com.example.gagaotalk.network.RetrofitClient
import com.example.gagaotalk.network.ServiceApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.item_friend.*
import kotlinx.android.synthetic.main.item_friend.txt_name
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener{

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Bottom_Navigation*/
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        //bottomnavigation 텍스트 제거
        bottom_navigation.labelVisibilityMode = LABEL_VISIBILITY_UNLABELED
        //시작 탭 결정
        bottom_navigation.selectedItemId = R.id.action_home

        //사용자 정보 받아오기
//        auth = FirebaseAuth.getInstance()
//        user = auth.currentUser!!
//        Log.e("연결상태 확인",user.email)
        Log.e("연결상태 확인", App.prefs.myEmail)

        /*Button Listener*/
        btn_search.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }

        btn_add.setOnClickListener {
            startActivity(Intent(this,AddFriendActivity::class.java))
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        App.friendDTOList.clear()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                var FragmentA = FriendFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, FragmentA).commit()

                txtview.text = getString(R.string.friend)
                return true
            }

            R.id.action_search -> {
                var FragmentB = DialogsFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, FragmentB).commit()

                txtview.text = getString(R.string.chat)
                return true
            }

            R.id.action_account -> {
                return true
            }
        }
        return false
    }

}
