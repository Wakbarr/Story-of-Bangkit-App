package com.example.storyapp__subs1.ui.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.remote.Api
import com.example.storyapp__subs1.data.repository.Repouser
import com.example.storyapp__subs1.data.respons.ListStoryItem
import com.example.storyapp__subs1.data.respons.StoryRespons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: Repouser) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories


    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getSession(): LiveData<Model> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val client = Api.getApiService().getAllStories(null, null, 0, token)
            client.enqueue(object : Callback<StoryRespons> {
                override fun onResponse(
                    call: Call<StoryRespons>,
                    response: Response<StoryRespons>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        val storiesRequest = response.body()
                        if (storiesRequest != null && !storiesRequest.error) {
                            Log.d("storiesRequest", "Fetch Stories Success: ${storiesRequest.message}")
                            _stories.value = storiesRequest.listStory
                            _message.value = storiesRequest.message
                        } else {
                            Log.d("storiesRequest", "storiesRequest: ${storiesRequest?.message}")
                            _message.value = storiesRequest?.message
                        }
                    } else {
                        _isLoading.value = false
                        Log.d("storiesRequest", "storiesRequest: ${response.message()}")
                        _message.value = "Error Occur, Try Again Later"
                    }
                }


                override fun onFailure(call: Call<StoryRespons>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("storiesRequest", "Error: ${t.message}")
                    _message.value = "Error: No Internet"
                }
            })
        }
    }
}