package com.astriex.reflection.ui.viewModels

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.repository.FirebaseRepository
import kotlinx.coroutines.launch

class LoginRegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val loading: LiveData<Boolean>
        get() = repository.isLoading
    val userLoggedIn: LiveData<Boolean> = repository.loggedIn
    val currentUsername: LiveData<String> = repository.currentUsername
    val message: LiveData<String> = repository.message

    fun registerUser(email: String, password: String, username: String) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            viewModelScope.launch {
                repository.registerUser(email, password, username)
            }
        } else {

        }
    }

}