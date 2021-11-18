package com.astriex.reflection.presentation.activity.editNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.domain.model.Note
import com.astriex.reflection.domain.use_case.note_use_case.NoteUseCases
import com.astriex.reflection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(private val noteUseCases: NoteUseCases) :
    ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading
    var result = MutableLiveData<Result>()
    var message = String()

    fun resetResult() {
        result = MutableLiveData<Result>()
    }

    fun updateNote(note: Note): LiveData<Result> {
        viewModelScope.launch {
            _isLoading.postValue(true)
            result.value = noteUseCases.editNoteUseCase(note)
            _isLoading.postValue(false)
        }
        return result
    }

    fun getFormattedDate(note: Note): String = noteUseCases.editNoteUseCase.getFormattedDate(note)

}