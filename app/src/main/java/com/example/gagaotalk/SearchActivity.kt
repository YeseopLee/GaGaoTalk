package com.example.gagaotalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gagaotalk.Model.App
import com.example.gagaotalk.Model.FriendDTO
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_search.*

val friendDTOList : ArrayList<FriendDTO> = arrayListOf()

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // edittext 포커스 제거 미완성


        // 버튼 리스너
        btn_saerchclose.setOnClickListener {
            finish()
        }

        var searchAdapter = FriendsListAdapter(this, App.friendDTOList)
        searchactivity_recyclerview.adapter = searchAdapter

        val lm = LinearLayoutManager(this)
        searchactivity_recyclerview.layoutManager = lm
        searchactivity_recyclerview.setHasFixedSize(true)

        edittext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                searchAdapter.filter.filter(p0)

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchAdapter.filter.filter(p0)
            }
        })

//        searchview.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchAdapter.filter.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                searchAdapter.filter.filter(newText)
//                System.out.println(friendDTOList)
//                System.out.println(searchAdapter.filter.filter(newText))
//                return false
//            }
//
//        })

    }
}
