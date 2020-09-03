package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class JoinDTO(
    @SerializedName("userName") val userName : String,
    @SerializedName("userEmail") val userEmail : String,
    @SerializedName("userPwd") val userPwd : String
)