package com.astriex.reflection.ui.activities.notesList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.repositories.FirebaseRepository
import kotlinx.coroutines.launch
import com.astriex.reflection.util.Result

class NotesListViewModel(private val repository: FirebaseRepository) : ViewModel() {
    var result = MutableLiveData<Result>()

    fun signOut() = repository.signOut()

    fun loadNotes() = repository.loadNotebookData()

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            result.value = repository.deleteNote(note)
        }
    }



}