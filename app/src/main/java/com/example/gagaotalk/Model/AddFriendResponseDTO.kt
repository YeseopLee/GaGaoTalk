package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class AddFriendResponseDTO(

    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String

)