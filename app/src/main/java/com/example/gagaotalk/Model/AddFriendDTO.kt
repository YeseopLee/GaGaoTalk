package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class AddFriendDTO(
    @SerializedName("userEmail") val userEmail : String,
    @SerializedName("friendEmail") val friendEmail : String
)