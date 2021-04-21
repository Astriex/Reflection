package com.astriex.reflection.ui.activities.intro

import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.repositories.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel() {

    fun getCurrentUser() = repository.getCurrentUser()

}