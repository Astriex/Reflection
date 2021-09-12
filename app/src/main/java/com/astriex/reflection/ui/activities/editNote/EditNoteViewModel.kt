package com.astriex.reflection.ui.activities.editNote

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel() {
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
            result.value = repository.updateNote(note)
            _isLoading.postValue(false)
        }
        return result
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

    @SuppressLint("SimpleDateFormat")
    fun getFormattedDate(note: Note): String {
        val date = note.timeAdded!!.toDate()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return sdf.format(date)
    }

}