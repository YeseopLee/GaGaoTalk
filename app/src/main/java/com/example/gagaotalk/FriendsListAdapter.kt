package com.example.gagaotalk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.gagaotalk.Model.FriendDTO
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.item_friend.view.*
import java.util.*
import kotlin.collections.ArrayList

class FriendsListAdapter(val context: Context, val friendDTO : ArrayList<FriendDTO>, val username:String = "none") : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable{

    var friendDTOfilter = ArrayList<FriendDTO>()

    init {
        friendDTOfilter = friendDTO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false)
                CustomViewHolder(view)
            }

            1 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_friend,parent,false)
                CustomViewHolder(view)
            }

            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return friendDTOfilter.size
    }

    override fun getItemViewType(position: Int): Int {
        return friendDTOfilter[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        view.txt_name.text = friendDTOfilter[position].name
        view.txt_status.text = friendDTOfilter[position].status

        view.profile_layout.setOnClickListener {
            val iT = Intent(context,ProfileActivity::class.java)
            iT.putExtra("name",friendDTOfilter[position].name)
            iT.putExtra("username",username)
            context.startActivity(iT)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    friendDTOfilter = friendDTO
                } else {
                    val resultList = ArrayList<FriendDTO>()
                    for (row in friendDTO)
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                    }
                    friendDTOfilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = friendDTOfilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                friendDTOfilter = results?.values as ArrayList<FriendDTO>
                notifyDataSetChanged()
            }

        }

    }
}