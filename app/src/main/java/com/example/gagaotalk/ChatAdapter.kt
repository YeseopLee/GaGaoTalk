package com.example.gagaotalk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.gagaotalk.Model.ChatDTO
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlinx.android.synthetic.main.item_userchat.view.*
import kotlinx.android.synthetic.main.item_userchat.view.chat_Time
import kotlinx.android.synthetic.main.item_userchat_reverse.view.*
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(val context: Context, val chatDTO : ArrayList<ChatDTO>, val username : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable{

    var chatDTOfilter = ArrayList<ChatDTO>()

    init {
        chatDTOfilter = chatDTO
    }

    fun addItem(item: ChatDTO) {//아이템 추가
        if (chatDTOfilter != null) {
            chatDTOfilter.add(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_userchat,parent,false)
                MyChatViewHolder(view)
            }

            1 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_userchat_reverse,parent,false)
                YourChatViewHolder(view)
            }

            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class MyChatViewHolder(view : View) : RecyclerView.ViewHolder(view)
    inner class YourChatViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return chatDTOfilter.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(chatDTOfilter[position].name == username){
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is MyChatViewHolder){
            var view = holder.itemView
            view.chat_Text.setText(chatDTOfilter[position].script)
            view.chat_Time.setText(chatDTOfilter[position].date)
        }

        else if(holder is YourChatViewHolder){
            var view = holder.itemView
            view.chat_You_Name.setText(chatDTOfilter[position].name)
            view.chat_You_Text.setText(chatDTOfilter[position].script)
            view.chat_You_Time.setText(chatDTOfilter[position].date)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    chatDTOfilter = chatDTO
                } else {
                    val resultList = ArrayList<ChatDTO>()
                    for (row in chatDTO)
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                    }
                    chatDTOfilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = chatDTOfilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                chatDTOfilter = results?.values as ArrayList<ChatDTO>
                notifyDataSetChanged()
            }

        }

    }
}