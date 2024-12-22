package com.example.storyapp__subs1.data.preferensi

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class prefuser private constructor(private val dataStore: DataStore<Preferences>) {


    companion object {
        @Volatile
        private var INSTANCE: prefuser? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val NAME = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("userID")

        fun getInstance(dataStore: DataStore<Preferences>): prefuser {
            return INSTANCE ?: synchronized(this) {
                val instance = prefuser(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveSession(user: Model) {
        dataStore.edit { preferences ->
            preferences[NAME] = user.name
            preferences[USER_ID] = user.userId
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = user.isLogin
        }
    }

    fun getSession(): Flow<Model> {
        return dataStore.data.map { preferences ->
            Model(
                preferences[NAME] ?: "",
                preferences[USER_ID] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


}