package com.example.storyapp__subs1.data.remote

import com.example.storyapp__subs1.data.respons.AddStoryRespons
import com.example.storyapp__subs1.data.respons.LoginRespons
import com.example.storyapp__subs1.data.respons.RegisterRespons
import com.example.storyapp__subs1.data.respons.StoryRespons
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginRespons>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterRespons>

    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0,
        @Header("Authorization") token: String
    ): Call<StoryRespons>

    @GET("stories")
    suspend fun getAllPagingStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0,
        @Header("Authorization") token: String
    ): StoryRespons

    @Multipart
    @POST("stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Header("Authorization") token: String
    ): Call<AddStoryRespons>

    @Multipart
    @POST("stories")
    fun postStoryWithLoc(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<AddStoryRespons>
}