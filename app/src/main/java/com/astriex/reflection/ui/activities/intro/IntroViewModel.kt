package com.astriex.reflection.ui.activities.intro

import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.repositories.FirebaseRepository

class IntroViewModel(private val repository: FirebaseRepository) : ViewModel() {

    fun getCurrentUser() = repository.getCurrentUser()

}