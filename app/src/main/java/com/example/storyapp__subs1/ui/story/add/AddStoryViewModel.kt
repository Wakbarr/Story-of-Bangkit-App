package com.example.storyapp__subs1.ui.story.add

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.remote.Api
import com.example.storyapp__subs1.data.repository.Repouser
import com.example.storyapp__subs1.data.respons.AddStoryRespons
import com.example.storyapp__subs1.utils.reduceFileImage
import com.example.storyapp__subs1.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val repository: Repouser) : ViewModel() {

    companion object {
        private const val TAG = "AddStoryViewModel"
    }

    var currentImageUri: Uri? = null


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun uploadImage(imgDesc: String, context: Context, token: String) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile( uri, context).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            val requestBody = imgDesc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            viewModelScope.launch {
                _isLoading.value = true
                _isSuccess.value = false
                val client = Api.getApiService().postImage(multipartBody, requestBody, token)
                client.enqueue(object : Callback<AddStoryRespons> {
                    override fun onResponse(
                        call: Call<AddStoryRespons>,
                        response: Response<AddStoryRespons>
                    ) {
                        if (response.isSuccessful) {
                            _isLoading.value = false
                            val addStoryresponse = response.body()

                            if (addStoryresponse != null) {
                                if (addStoryresponse.error == false) {
                                    _isSuccess.value = true
                                    _message.value = addStoryresponse.message
                                } else {
                                    Log.e(TAG, "Response body is null")
                                    _message.value = addStoryresponse.message
                                }
                            }

                        } else {
                            Log.e(TAG, "Response body is null")
                            _message.value = "Error Occur, Please Try Again"
                        }
                    }

                    override fun onFailure(call: Call<AddStoryRespons>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message}")
                        _isLoading.value = true
                        _message.value = "Error: No Internet"
                    }
                })
            }

        } ?: run { _message.value = "No Image Added" }
    }


    fun getSession(): LiveData<Model> {
        return repository.getSession().asLiveData()
    }
}