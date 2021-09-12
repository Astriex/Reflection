package com.astriex.reflection.ui.activities.register

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
class RegisterViewModel @Inject constructor(val repository: FirebaseRepository) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading
    var result = MutableLiveData<Result>()
    var message = String()

    // Prevent repeat of login btn snackbar message on config change
    fun resetResult() {
        result = MutableLiveData<Result>()
    }

    fun registerUser(email: String, password: String, username: String): LiveData<Result> {
        viewModelScope.launch {
            _isLoading.postValue(true)
            result.value = repository.registerUser(email, password, username)
            _isLoading.postValue(false)
        }
        return result
    }

    fun isDataValid(username: String, email: String, password: String): Boolean {
        var isValid = false

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            message = "Fields cannot be empty"
        } else {
            isValid = true
        }
        return isValid
    }

}