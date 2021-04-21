package com.astriex.reflection.ui.activities.notesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    var result = MutableLiveData<Result>()

    fun resetResult() {
        result = MutableLiveData<Result>()
    }

    fun signOut() = repository.signOut()

    fun loadNotes() = repository.loadNotebookData()

    fun deleteNote(note: Note): LiveData<Result> {
        viewModelScope.launch {
            result.value = repository.deleteNote(note)
        }
        return result
    }
}