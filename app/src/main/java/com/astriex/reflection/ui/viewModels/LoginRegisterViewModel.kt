package com.astriex.reflection.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.repository.FirebaseRepository

class LoginRegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading as LiveData<Boolean>

    fun registerUser(email: String, password: String, username: String) {
        _isLoading.postValue(true)
    }

}