package com.example.storyapp__subs1.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp__subs1.data.repository.StoryRepository
import com.example.storyapp__subs1.data.remote.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StoryRepository(Api.getApiService())
    private val _stories = MutableLiveData<List<StoryRepository>>()
    val stories: LiveData<List<StoryRepository>> = _stories

    fun fetchStoriesByLocation(token: String, location: Int) {
        repository.getStoriesByLocation(token, location) { result ->
            result.onSuccess { storyList ->
                _stories.postValue(storyList)
            }.onFailure { error ->
                // Tambahkan logging atau notifikasi error
                error.printStackTrace()
            }
        }
    }
}
