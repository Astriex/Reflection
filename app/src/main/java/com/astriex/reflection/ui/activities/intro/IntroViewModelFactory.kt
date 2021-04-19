package com.astriex.reflection.ui.activities.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.data.repositories.FirebaseRepository

class IntroViewModelFactory(private val repository: FirebaseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IntroViewModel::class.java)) {
            return IntroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}