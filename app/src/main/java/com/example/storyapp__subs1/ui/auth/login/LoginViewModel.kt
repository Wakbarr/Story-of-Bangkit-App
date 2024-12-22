package com.example.storyapp__subs1.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.remote.Api
import com.example.storyapp__subs1.data.repository.Repouser
import com.example.storyapp__subs1.data.respons.LoginRespons
import com.example.storyapp__subs1.data.respons.LoginResult
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: Repouser) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginValue = MutableLiveData<LoginResult>()
    val loginValue: LiveData<LoginResult> = _loginValue

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun saveSession(user: Model) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginRequest(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val client = Api.getApiService().login(email, password)
            client.enqueue(object : Callback<LoginRespons> {
                override fun onResponse(
                    call: Call<LoginRespons>,
                    response: Response<LoginRespons>
                ) {

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _isLoading.value = false
                        if (loginResponse != null) {
                            Log.d("LoginRequest", "Login Success: ${loginResponse.message}")
                            _loginValue.value = loginResponse.loginResult
                            _message.value = loginResponse?.message
                        } else {
                            Log.d("LoginRequest", "Login Failed: ${loginResponse?.message}")
                            _message.value = loginResponse?.message
                        }
                    } else {
                        _isLoading.value = false
                        Log.d("LoginRequest", "Login Failed: ${response.message()}")
                        _message.value = "mail Has Been Taken or Invalid Password"
                    }
                }

                override fun onFailure(call: Call<LoginRespons>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("LoginRequest", "Error: ${t.message}")
                    _message.value = "Error: No Intenet"
                }
            })
        }
    }


}