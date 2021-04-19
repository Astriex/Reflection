package com.astriex.reflection.ui.activities.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.util.Result
import kotlinx.coroutines.launch

class RegisterViewModel(val repository: FirebaseRepository): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    var result = MutableLiveData<Result>()

    suspend fun registerUser(email: String, password: String, username: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            result.value = repository.registerUser(email, password, username)
            isLoading.postValue(false)
        }
    }
}