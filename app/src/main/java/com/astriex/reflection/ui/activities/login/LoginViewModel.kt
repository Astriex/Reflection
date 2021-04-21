package com.astriex.reflection.ui.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData(false)
    var result = MutableLiveData<Result>()
    var message = String()

    // prevent repeat of toast message
    fun resetResult() {
        result = MutableLiveData<Result>()
    }

    fun loginUser(email: String, password: String): LiveData<Result> {
        viewModelScope.launch {
            isLoading.postValue(true)
            result.value = repository.loginUser(email, password)
            isLoading.postValue(false)
        }
        return result
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