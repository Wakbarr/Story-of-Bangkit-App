package com.example.storyapp__subs1.ui.auth.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.repository.Result
import com.example.storyapp__subs1.data.repository.UserStoryRepo
import com.example.storyapp__subs1.data.respons.LoginResult
import com.example.storyapp__subs1.di.Inject
import kotlinx.coroutines.launch

class LoginViewModel(private val userStoryRepository: UserStoryRepo) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userStoryRepository.logout()
        }
    }

    fun saveSession(user: Model) {
        viewModelScope.launch {
            userStoryRepository.saveSession(user)
        }

    }

    fun loginRequest(email: String, password: String): LiveData<Result<LoginResult>> {
        return userStoryRepository.loginRequest(email, password)
    }

}
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(Inject.provideRepouser(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
