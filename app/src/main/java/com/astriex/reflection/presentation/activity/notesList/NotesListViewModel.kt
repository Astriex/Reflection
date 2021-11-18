package com.astriex.reflection.presentation.activity.notesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.domain.model.Note
import com.astriex.reflection.domain.use_case.note_use_case.NoteUseCases
import com.astriex.reflection.domain.use_case.user_use_case.UserUseCases
import com.astriex.reflection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val notesUseCases: NoteUseCases,
    private val userUseCases: UserUseCases
) :
    ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading
    var result = MutableLiveData<Result>()

    fun resetResult() {
        result = MutableLiveData<Result>()
    }

    fun signOut() = userUseCases.logoutUserUseCase()

    fun loadNotes() = notesUseCases.loadNotesListUseCase()

    fun deleteNote(note: Note): LiveData<Result> {
        viewModelScope.launch {
            result.value = notesUseCases.deleteNoteUseCase(note)
        }
        return result
    }

    fun startLoading() {
        _isLoading.postValue(true)
    }

    fun stopLoading() {
        _isLoading.postValue(false)
    }
}