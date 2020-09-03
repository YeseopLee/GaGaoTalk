package com.example.gagaotalk.network

import com.example.gagaotalk.Model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ServiceApi {
    @POST("/user/login")
    fun userLogin(@Body data: LoginDTO?): Call<LoginResponseDTO?>?

    @POST("/user/join")
    fun userJoin(@Body data: JoinDTO?): Call<JoinResponseDTO?>?

    @POST("/user/loadfriend")
    fun loadFriend(@Body data: FriendsDTO?): Call<FriendsResponseDTO>?

    @POST("/user/loadfriend/setstatus")
    fun loadStatus(@Body data: UserStatusDTO?): Call<UserStatusResponseDTO>?


}