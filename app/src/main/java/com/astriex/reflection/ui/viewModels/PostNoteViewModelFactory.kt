package com.astriex.reflection.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.data.repository.FirebaseRepository

class PostNoteViewModelFactory(private var repository: FirebaseRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PostNoteViewModel::class.java)){
            return PostNoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}