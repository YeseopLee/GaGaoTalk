package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class UserStatusDTO(
    @SerializedName("userEmail") val userEmail : String
)