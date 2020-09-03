
package com.example.gagaotalk


import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gagaotalk.Model.App
import com.example.gagaotalk.Model.FriendDTO
import com.example.gagaotalk.Model.FriendsDTO
import com.example.gagaotalk.Model.FriendsResponseDTO
import com.example.gagaotalk.network.ServiceApi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_friend.*
import kotlinx.android.synthetic.main.fragment_friend.view.*
import kotlinx.android.synthetic.main.item_friend.*
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlinx.android.synthetic.main.item_friend.view.txt_name
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class FriendFragment : Fragment() {

    lateinit var username: String
    lateinit var prefs: PreferenceUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        prefs = PreferenceUtil(context!!)
        username = prefs.getString("username","noname")

        val db = FirebaseFirestore.getInstance()

        //Log.e("카운트",result.friendList.count().toString())


        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_friend, container, false )

        // adapter 연결
        view.friendfragment_recyclerview.adapter = FriendsListAdapter(activity!!,App.friendDTOList,username)
        view.friendfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        view.friendfragment_recyclerview.setHasFixedSize(true)

        return view
    }



}

