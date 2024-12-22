package com.example.storyapp__subs1.data.repository

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp__subs1.di.Inject
import com.example.storyapp__subs1.ui.auth.login.LoginViewModel
import com.example.storyapp__subs1.ui.core.MainViewModel
import com.example.storyapp__subs1.ui.story.add.AddStoryViewModel

class UserViewModelFactory(private val repository: Repouser) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): UserViewModelFactory {
            if (INSTANCE == null) {
                synchronized(UserViewModelFactory::class.java) {
                    INSTANCE = UserViewModelFactory(Inject.provideRepouser(context))
                }
            }
            return INSTANCE as UserViewModelFactory
        }
    }
}