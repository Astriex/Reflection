package com.astriex.reflection.ui.viewModels

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.repositories.FirebaseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginRegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    fun registerUser(email: String, password: String, username: String) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            viewModelScope.launch {
                isLoading.postValue(true)
                repository.registerUser(email, password, username)
                delay(3000)
                isLoading.postValue(false)
            }
        }
    }

}