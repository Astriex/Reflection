package com.astriex.reflection.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.data.repositories.FirebaseRepository

class LoginRegisterViewModelFactory(private val repository: FirebaseRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginRegisterViewModel::class.java)){
            return LoginRegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}