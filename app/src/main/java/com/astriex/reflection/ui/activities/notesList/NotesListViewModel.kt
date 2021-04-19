package com.astriex.reflection.ui.activities.notesList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.repositories.FirebaseRepository

class NotesListViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    fun signOut() = repository.signOut()

    fun loadNotes() = repository.loadNotebookData()

}