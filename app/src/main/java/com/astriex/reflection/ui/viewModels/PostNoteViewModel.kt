package com.astriex.reflection.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.repository.FirebaseRepository

class PostNoteViewModel(private val repository: FirebaseRepository): ViewModel() {
    val isLoading: LiveData<Boolean>
        get() = repository.isLoading


}