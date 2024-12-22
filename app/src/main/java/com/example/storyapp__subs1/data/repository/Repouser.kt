package com.example.storyapp__subs1.data.repository

import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.preferensi.prefuser
import kotlinx.coroutines.flow.Flow

class Repouser private constructor(
    private val userPreference: prefuser
) {

    suspend fun saveSession(user: Model) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<Model> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repouser? = null
        fun getInstance(
            userPreference: prefuser
        ): Repouser =
            instance ?: synchronized(this) {
                instance ?: Repouser(userPreference)
            }.also { instance = it }
    }
}