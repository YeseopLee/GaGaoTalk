package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName


data class LoginDTO(

    @SerializedName("userEmail") val userEmail: String,
    @SerializedName("userPwd") val userPwd: String

)