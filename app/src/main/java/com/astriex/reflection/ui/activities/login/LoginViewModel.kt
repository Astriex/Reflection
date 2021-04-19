package com.astriex.reflection.ui.activities.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.util.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val result = MutableLiveData<Result>()
    var message = String()

    suspend fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            result.value = repository.loginUser(email, password)
            isLoading.postValue(false)
        }
    }

    fun isDataValid(email: String, password: String): Boolean {
        var isValid = false

        if (email.isEmpty() || password.isEmpty()) {
            message = "Fields cannot be empty"
        } else {
            isValid = true
        }
        return isValid
    }

}