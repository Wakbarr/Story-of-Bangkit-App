package com.example.storyappsubs1.data.repository

import com.example.storyapp__subs1.data.remote.ApiService
import com.example.storyapp__subs1.data.respons.StoryRespons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val apiService: ApiService) {

    fun getStoriesByLocation(token: Int, location: Int,  callback: (Result<List<StoryRespons>>) -> Unit) {
        apiService.getAllStories(location, token).enqueue(object : Callback<StoryRespons> {
            override fun onResponse(call: Call<StoryRespons>, response: Response<StoryRespons>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it.))
                    } ?: callback(Result.failure(Throwable("No data")))
                } else {
                    callback(Result.failure(Throwable("Failed to load data")))
                }
            }

            override fun onFailure(call: Call<StoryRespons>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}