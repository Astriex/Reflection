package com.astriex.reflection.ui.viewModels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.repository.FirebaseRepository

class LoginRegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val loading: LiveData<Boolean>
        get() = repository.isLoading
    private lateinit var registrationDone: LiveData<Boolean>

    fun registerUser(email: String, password: String, username: String): LiveData<Boolean> {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            registrationDone = repository.registerUser(email, password, username)
        }
        return registrationDone
    }

}