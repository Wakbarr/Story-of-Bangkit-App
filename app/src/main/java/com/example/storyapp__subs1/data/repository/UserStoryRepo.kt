package com.example.storyapp__subs1.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.preferensi.prefuser
import com.example.storyapp__subs1.data.remote.ApiService
import com.example.storyapp__subs1.data.respons.AddStoryRespons
import com.example.storyapp__subs1.data.respons.ListStoryItem
import com.example.storyapp__subs1.data.respons.LoginRespons
import com.example.storyapp__subs1.data.respons.LoginResult
import com.example.storyapp__subs1.data.respons.RegisterRespons
import com.example.storyapp__subs1.data.respons.StoryRespons
import com.example.storyapp__subs1.utils.reduceFileImage
import com.example.storyapp__subs1.utils.uriToFile
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserStoryRepo(
    private val userPreference: prefuser,
    private val apiService: ApiService
) {

    // Session Management
    suspend fun saveSession(user: Model) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<Model> = userPreference.getSession()

    suspend fun logout() {
        userPreference.logout()
    }

    // Pagination
    fun getPagingStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 2),
            pagingSourceFactory = { com.example.storyapp__subs1.data.respons.PagingSource(apiService, token) }
        ).liveData
    }

    // MediatorLiveData for Observing Results
    private val resultGetStories = MediatorLiveData<Result<List<ListStoryItem>>>()
    private val resultLoginRequest = MediatorLiveData<Result<LoginResult>>()
    private val resultUploadStory = MediatorLiveData<Result<AddStoryRespons>>()
    private val resultRegisterRequest = MediatorLiveData<Result<RegisterRespons>>()

    // Fetch Stories
    fun getStories(location: Int, token: String): LiveData<Result<List<ListStoryItem>>> {
        resultGetStories.postValue(Result.Loading)
        val client = apiService.getAllStories(null, null, location, token)
        client.enqueue(object : Callback<StoryRespons> {
            override fun onResponse(call: Call<StoryRespons>, response: Response<StoryRespons>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && !body.error) {
                        resultGetStories.postValue(Result.Success(body.listStory))
                    } else {
                        resultGetStories.postValue(Result.Error(body?.message ?: "Unknown Error"))
                    }
                } else {
                    resultGetStories.postValue(Result.Error("Failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<StoryRespons>, t: Throwable) {
                resultGetStories.postValue(Result.Error("Network Error: ${t.message}"))
            }
        })
        return resultGetStories
    }

    // Login
    fun loginRequest(email: String, password: String): LiveData<Result<LoginResult>> {
        resultLoginRequest.postValue(Result.Loading)
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginRespons> {
            override fun onResponse(call: Call<LoginRespons>, response: Response<LoginRespons>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        resultLoginRequest.postValue(Result.Success(body.loginResult))
                    } else {
                        resultLoginRequest.postValue(Result.Error("Invalid response"))
                    }
                } else {
                    resultLoginRequest.postValue(Result.Error("Failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<LoginRespons>, t: Throwable) {
                resultLoginRequest.postValue(Result.Error("Network Error: ${t.message}"))
            }
        })
        return resultLoginRequest
    }

    // Upload Story
    private fun createUploadRequestBody(
        imgDesc: String,
        context: Context,
        imageUri: Uri
    ): MultipartBody.Part {
        val file = uriToFile(imageUri, context).reduceFileImage()
        val requestBody = file.asRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData("photo", file.name, requestBody)
    }

    private fun handleUploadStoryResponse(
        call: Call<AddStoryRespons>,
        liveData: MediatorLiveData<Result<AddStoryRespons>>
    ) {
        call.enqueue(object : Callback<AddStoryRespons> {
            override fun onResponse(call: Call<AddStoryRespons>, response: Response<AddStoryRespons>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        liveData.postValue(Result.Success(it))
                    } ?: liveData.postValue(Result.Error("Invalid Response"))
                } else {
                    liveData.postValue(Result.Error("Failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<AddStoryRespons>, t: Throwable) {
                liveData.postValue(Result.Error("Network Error: ${t.message}"))
            }
        })
    }

    fun uploadStory(
        imgDesc: String,
        context: Context,
        token: String,
        imageUri: Uri,
        lat: Double,
        lon: Double
    ): LiveData<Result<AddStoryRespons>> {
        resultUploadStory.postValue(Result.Loading)
        val requestBody = createUploadRequestBody(imgDesc, context, imageUri)
        val description = imgDesc.toRequestBody("text/plain".toMediaType())
        val latitude = lat.toString().toRequestBody("text/plain".toMediaType())
        val longitude = lon.toString().toRequestBody("text/plain".toMediaType())

        val call = apiService.postStory(requestBody, description, latitude, longitude, token)
        handleUploadStoryResponse(call, resultUploadStory)
        return resultUploadStory
    }

    fun uploadStoryWithoutLoc(
        imgDesc: String,
        context: Context,
        token: String,
        imageUri: Uri
    ): LiveData<Result<AddStoryRespons>> {
        resultUploadStory.postValue(Result.Loading)
        val requestBody = createUploadRequestBody(imgDesc, context, imageUri)
        val description = imgDesc.toRequestBody("text/plain".toMediaType())

        val call = apiService.postStoryWithLoc(requestBody, description, token)
        handleUploadStoryResponse(call, resultUploadStory)
        return resultUploadStory
    }

    // Register
    fun registerRequest(name: String, email: String, password: String): LiveData<Result<RegisterRespons>> {
        resultRegisterRequest.postValue(Result.Loading)
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<RegisterRespons> {
            override fun onResponse(call: Call<RegisterRespons>, response: Response<RegisterRespons>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        resultRegisterRequest.postValue(Result.Success(it))
                    } ?: resultRegisterRequest.postValue(Result.Error("Invalid Response"))
                } else {
                    resultRegisterRequest.postValue(Result.Error("Failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<RegisterRespons>, t: Throwable) {
                resultRegisterRequest.postValue(Result.Error("Network Error: ${t.message}"))
            }
        })
        return resultRegisterRequest
    }
}
