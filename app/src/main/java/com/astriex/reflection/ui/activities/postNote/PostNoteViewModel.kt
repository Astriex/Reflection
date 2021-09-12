package com.astriex.reflection.ui.activities.postNote

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.models.User
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostNoteViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    val isLoading = MutableLiveData(false)
    val userData: LiveData<User> = repository.userData
    var result = MutableLiveData<Result>()
    var message = String()

    init {
        repository.getUserData()
    }

    suspend fun saveNote(title: String, content: String, imageUri: Uri?, username: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            result.value = repository.saveNote(title, content, imageUri!!, username)
            isLoading.postValue(false)
        }
    }

    fun isDataValid(title: String, content: String, imageUri: Uri?): Boolean {
        var isValid = false

        if (title.isNotEmpty() && content.isNotEmpty() && imageUri != null) {
            isValid = true
        } else {
            isValid = false
            if (title.isEmpty() || content.isEmpty()) {
                message = "Fields cannot be empty"
            }
            if (imageUri == null) {
                message = "Please select a photo"
            }
        }
        return isValid
    }

}