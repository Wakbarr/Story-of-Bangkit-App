package com.example.storyapp__subs1.di

import android.content.Context
import com.example.storyapp__subs1.data.preferensi.dataStore
import com.example.storyapp__subs1.data.preferensi.prefuser
import com.example.storyapp__subs1.data.remote.Api
import com.example.storyapp__subs1.data.repository.UserStoryRepo

object Inject {
    fun provideRepouser(context: Context): UserStoryRepo {
        val pref = prefuser.getInstance(context.dataStore)
        val apiService = Api.getApiService()
        return UserStoryRepo(pref,apiService)
    }
}