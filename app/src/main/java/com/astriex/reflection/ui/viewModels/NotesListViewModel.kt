package com.astriex.reflection.ui.viewModels

import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.repositories.FirebaseRepository

class NotesListViewModel(private val repository: FirebaseRepository): ViewModel() {

    fun signOut() = repository.signOut()

}