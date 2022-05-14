package me.aravi.instagramx.api

import me.aravi.instagramx.InstaConfig
import retrofit2.Call
import retrofit2.http.*


interface InstagramApi {

    @GET("{username}/?__a=1")
    fun profile(
        @Path("username") username: String,
        @Header("Cookie") cookie: String,
        @Header("x-csrftoken") csrfToken: String,
        @Header("X-Instagram-AJAX") roll_hash: String,
        @Header("user-agent") user_agent: String = InstaConfig.USER_AGENT
    ): Call<String>


    @GET("graphql/query/")
    fun posts(
        @Header("Cookie") cookie: String,
        @Header("X-CSRFtoken") csrfToken: String,
        @Header("User-Agent") user_agent: String = InstaConfig.USER_AGENT,
        @Header("X-Instagram-AJAX") roll_hash: String,
        @Query("query_hash") queryHash: String = InstaConfig.QUERY_HASH_POSTS,
        @Query("variables") variables: String
    ): Call<String>

    @GET("graphql/query/")
    fun followee(
        @Header("Cookie") cookie: String,
        @Header("X-CSRFtoken") csrfToken: String,
        @Header("User-Agent") user_agent: String = InstaConfig.USER_AGENT,
        @Header("X-Instagram-AJAX") roll_hash: String,
        @Query("query_hash") queryHash: String = InstaConfig.QUERY_HASH_FOLLOWING,
        @Query("variables") variables: String
    ): Call<String>

    @GET("graphql/query/")
    fun followers(
        @Header("Cookie") cookie: String,
        @Header("X-CSRFtoken") csrfToken: String,
        @Header("User-Agent") user_agent: String = InstaConfig.USER_AGENT,
        @Header("X-Instagram-AJAX") roll_hash: String,
        @Query("query_hash") queryHash: String = InstaConfig.QUERY_HASH_FOLLOWERS,
        @Query("variables") variables: String
    ): Call<String>

    @GET("p/{post_id}/?__a=1")
    fun post(
        @Path("post_id") username: String,
        @Header("Cookie") cookie: String,
        @Header("x-csrftoken") csrfToken: String,
        @Header("X-Instagram-AJAX") roll_hash: String,
        @Header("user-agent") user_agent: String = InstaConfig.USER_AGENT
    ): Call<String>


    @POST("web/likes/{post_id}/like/")
    fun like(
        @Header("Cookie") cookie: String,
        @Header("X-Csrftoken") csrfToken: String,
        @Header("X-Instagram-AJAX") roll_hash: String,
        @Header("user-agent") agent: String = InstaConfig.USER_AGENT,
        @Path("post_id") postId: String
    ): Call<String>

    @POST("web/friendships/{user_id}/follow/")
    fun follow(
        @Header("Cookie") cookie: String,
        @Header("X-CSRFToken") csrfToken: String,
        @Header("X-Instagram-AJAX") roll_hash: String,
        @Header("User-Agent") agent: String = InstaConfig.USER_AGENT,
        @Path("user_id") userId: String
    ): Call<String>

}