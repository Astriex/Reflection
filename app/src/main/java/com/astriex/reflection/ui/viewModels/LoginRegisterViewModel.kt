package com.astriex.reflection.ui.viewModels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.repository.FirebaseRepository
import kotlinx.coroutines.launch

class LoginRegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val loading: LiveData<Boolean>
        get() = repository.isLoading
    val currentUsername: LiveData<String> = repository.currentUsername
    val registrationMessage: LiveData<String> = repository.registrationMessage
    private lateinit var check: LiveData<Boolean>

    fun registerUser(email: String, password: String, username: String): LiveData<Boolean> {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            check = repository.registerUser(email, password, username)
        } else {

        }
        return check
    }

}