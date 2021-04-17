package com.astriex.reflection.ui.viewModels

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.repositories.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    suspend fun registerUser(email: String, password: String, username: String) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            isLoading.postValue(true)
            withContext(Dispatchers.IO) {
                repository.registerUser(email, password, username)
            }
            isLoading.postValue(false)
        }
    }

    suspend fun loginUser(email: String, password: String) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            isLoading.postValue(true)
            withContext(Dispatchers.IO) {
                repository.loginUser(email, password)
            }
            isLoading.postValue(false)
        }
    }

}