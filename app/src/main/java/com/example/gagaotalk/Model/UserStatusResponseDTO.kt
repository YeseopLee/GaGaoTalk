package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class UserStatusResponseDTO(

    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("userMsg") val userMsg : String
)