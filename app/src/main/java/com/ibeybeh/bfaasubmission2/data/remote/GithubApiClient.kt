package com.ibeybeh.bfaasubmission2.data.remote

import com.ibeybeh.bfaasubmission2.data.model.DataUserItem
import com.ibeybeh.bfaasubmission2.data.model.ProfileUserItem
import com.ibeybeh.bfaasubmission2.data.model.UserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiClient {

    @GET("search/users")
    fun getSearchUser(
        @Header("Authorization") authorization: String,
        @Query("q") query: String
    ): Call<UserItem>

    @GET("users/{username}")
    fun getDetailUser(
        @Header("Authorization") authorization: String,
        @Path("username") username: String
    ): Call<ProfileUserItem>

    @GET("users/{username}/followers")
    fun getFollowerUser(
        @Header("Authorization") authorization: String,
        @Path("username") username: String
    ): Call<List<DataUserItem>>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Header("Authorization") authorization: String,
        @Path("username") username: String
    ): Call<List<DataUserItem>>
}