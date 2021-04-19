package com.astriex.reflection.ui.activities.editNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.data.repositories.FirebaseRepository

class EditNoteViewModelFactory(private val repository: FirebaseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            return EditNoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}