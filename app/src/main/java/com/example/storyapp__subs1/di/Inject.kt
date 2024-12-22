package com.example.storyapp__subs1.di

import android.content.Context
import com.example.storyapp__subs1.data.preferensi.dataStore
import com.example.storyapp__subs1.data.preferensi.prefuser
import com.example.storyapp__subs1.data.repository.Repouser

object Inject {
    fun provideRepouser(context: Context): Repouser {
        val pref = prefuser.getInstance(context.dataStore)
        return Repouser.getInstance(pref)
    }
}