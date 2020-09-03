package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class FriendsResponseDTO(

    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("friendList") val friendList : Array<String>

)