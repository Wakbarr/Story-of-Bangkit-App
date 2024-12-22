package com.example.storyapp__subs1.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp__subs1.data.remote.Api
import com.example.storyapp__subs1.data.respons.RegisterRespons
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel() : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun registerRequest(name: String, email: String, password: String) {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            val client = Api.getApiService().register(name, email, password)
            client.enqueue(object : Callback<RegisterRespons> {
                override fun onResponse(
                    call: Call<RegisterRespons>,
                    response: Response<RegisterRespons>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        _isLoading.value = false
                        if (registerResponse != null) {
                            Log.d(
                                "RegisterRequest",
                                "Register Success: ${registerResponse.message}"
                            )
                            _isSuccess.value = true
                            _message.value = registerResponse?.message
                        } else {
                            _isSuccess.value = false
                            Log.d(
                                "RegisterRequest",
                                "Register Failed: ${registerResponse?.message}"
                            )
                            _message.value = registerResponse?.message
                        }
                    } else {
                        _isLoading.value = false
                        _isSuccess.value = false
                        Log.d("RegisterRequest", "Register Failed: ${response.message()}")
                        _message.value = "Email Has Been Taken"
                    }
                }

                override fun onFailure(call: Call<RegisterRespons>, t: Throwable) {
                    Log.e("LoginRequest", "Error: ${t.message}")
                    _isSuccess.value = false
                    _isLoading.value = false
                    _message.value = "Error: No Internet"
                }
            })
        }
    }
}