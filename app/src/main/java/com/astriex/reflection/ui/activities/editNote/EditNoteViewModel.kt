package com.astriex.reflection.ui.activities.editNote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.util.Result
import kotlinx.coroutines.launch

class EditNoteViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    var result = MutableLiveData<Result>()
    var message = String()

    init {
        repository.loadUserData()
        repository.loadNotebookData()
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            isLoading.postValue(true)
            result.value = repository.updateNote(note)
            isLoading.postValue(false)
        }
    }

    fun isDataValid(title: String, content: String): Boolean {
        var isValid = false

        if (title.isNotEmpty() && content.isNotEmpty()) {
            isValid = true
        } else {
            isValid = false
            if (title.isEmpty() || content.isEmpty()) {
                message = "Fields cannot be empty"
            }
        }
        return isValid
    }

}