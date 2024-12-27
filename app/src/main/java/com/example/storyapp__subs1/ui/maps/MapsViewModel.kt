package com.example.storyapp__subs1.ui.maps

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.repository.Result
import com.example.storyapp__subs1.data.repository.UserStoryRepo
import com.example.storyapp__subs1.data.respons.ListStoryItem
import com.example.storyapp__subs1.di.Inject

class MapsViewModel(private val userStoryRepo: UserStoryRepo) : ViewModel() {
    fun getStories(token: String): LiveData<Result<List<ListStoryItem>>> {
        return userStoryRepo.getStories(1, token)
    }

    fun getSession(): LiveData<Model> {
        return userStoryRepo.getSession().asLiveData()
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(Inject.provideRepouser(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}