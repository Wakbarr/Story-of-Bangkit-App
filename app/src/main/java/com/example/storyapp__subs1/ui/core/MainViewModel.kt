package com.example.storyapp__subs1.ui.core

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.repository.UserStoryRepo
import com.example.storyapp__subs1.data.respons.ListStoryItem
import com.example.storyapp__subs1.di.Inject
import kotlinx.coroutines.launch

class MainViewModel(private val userStoryRepository: UserStoryRepo) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userStoryRepository.logout()
        }
    }


    fun getSession(): LiveData<Model> {
        return userStoryRepository.getSession().asLiveData()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getPagingStories(token: String): LiveData<PagingData<ListStoryItem>> {
        _isLoading.value = true
        return userStoryRepository.getPagingStories(token).cachedIn(viewModelScope)
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Inject.provideRepouser(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}