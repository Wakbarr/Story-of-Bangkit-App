package com.example.storyapp__subs1.ui.story.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.repository.Result
import com.example.storyapp__subs1.data.repository.UserStoryRepo
import com.example.storyapp__subs1.data.respons.AddStoryRespons
import com.example.storyapp__subs1.di.Inject
import com.example.storyapp__subs1.ui.core.MainViewModel

class AddStoryViewModel(private val userStoryRepo: UserStoryRepo) : ViewModel() {

    companion object {
        private const val TAG = "AddStoryViewModel"
    }

    var currentImageUri: Uri? = null

    fun getSession(): LiveData<Model> {
        return userStoryRepo.getSession().asLiveData()
    }

    fun getUploadStory(imgDesc:String, context: Context, token: String, imageUri: Uri, lat: Double, lon: Double): LiveData<com.example.storyapp__subs1.data.repository.Result<AddStoryRespons>> =
        userStoryRepo.uploadStory(imgDesc, context, token, imageUri, lat, lon)

    fun getUploadStoryWithoutLoc(imgDesc:String, context: Context, token: String, imageUri: Uri): LiveData<Result<AddStoryRespons>> =
        userStoryRepo.uploadStoryWithoutLoc(imgDesc, context, token, imageUri)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddStoryViewModel(Inject.provideRepouser(context)) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Inject.provideRepouser(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
