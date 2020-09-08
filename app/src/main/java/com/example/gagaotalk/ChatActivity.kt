package com.example.gagaotalk

import android.nfc.Tag
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gagaotalk.Model.App
import com.example.gagaotalk.Model.ChatDTO
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() {

    lateinit var mSocket: Socket
    lateinit var username: String
    lateinit var name: String
    lateinit var chatAdapter: ChatAdapter

    private var chatCount: Int = 0
    private var hasConnection: Boolean = false
    private val SERVER_URL = "http://ec2-18-191-138-44.us-east-2.compute.amazonaws.com:3000"

    var chatDTOList : ArrayList<ChatDTO> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        username = intent.getStringExtra("username")
        name = intent.getStringExtra("name")


        chatactivity_roomname.setText(name)

        chatAdapter = ChatAdapter(this,chatDTOList,username)

        try {
            mSocket = IO.socket(SERVER_URL)
        } catch (e: URISyntaxException) {
            Log.e("서버 연결 에러", e.reason)
        }

        chatactivity_recyclerview.adapter = chatAdapter

        val lm = LinearLayoutManager(this)
        chatactivity_recyclerview.layoutManager = lm
        chatactivity_recyclerview.setHasFixedSize(true)

        //savedInstanceState =?
        if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection")
        }

        if (hasConnection) {

        } else {
            //소켓연결
            mSocket.connect()

            mSocket.on("connect user", onNewUser)
            mSocket.on("chat message", onNewMessage)

            val userId = JSONObject()
            try {
                userId.put("username", App.prefs.myName+"")
                userId.put("roomName", "room_example")
                Log.e("username",username+ " Connected")

                mSocket.emit("connect user", userId)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        hasConnection = true

        chat_send_btn.setOnClickListener {
            sendMessage()
        }

    }

    internal var onNewUser: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val length = args.size

            if (length == 0) {
                return@Runnable
            }
            var username = args[0].toString()
            Log.e("유저네임 체크",username);
            try {
                val `object` = JSONObject(username)
                username = `object`.getString("username")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        })
    }

    internal var onNewMessage: Emitter.Listener = Emitter.Listener { args->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val name: String
            val script: String
            val profile_image: String
            val date_time: String
            try {
                Log.e("data", data.toString())
                name = data.getString("name")
                script = data.getString("script")
                profile_image = data.getString("profile_image")
                date_time = data.getString("date_time")

                val format = ChatDTO(name, script, profile_image, date_time)
                chatAdapter.addItem(format)
                chatAdapter.notifyDataSetChanged()
                chatactivity_recyclerview.scrollToPosition(chatCount - 1)
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }

    fun sendMessage(){
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val getTime = sdf.format(date)

        val message = chatactivity_edittext.getText().toString().trim({ it <= ' ' })
        Log.e("message",message)
        if (TextUtils.isEmpty(message)) {
            return
        }
        // 채팅 초기화
        chatactivity_edittext.setText("")
        chatCount++
        Log.e("chatcount",chatCount.toString())

        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", App.prefs.myName)
            jsonObject.put("script", message)
            jsonObject.put("profile_image", "example")
            jsonObject.put("date_time", getTime)
            jsonObject.put("roomName", "room_example")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Log.e("챗룸", "sendMessage: 1" + mSocket.emit("chat message", jsonObject))

    }
}
