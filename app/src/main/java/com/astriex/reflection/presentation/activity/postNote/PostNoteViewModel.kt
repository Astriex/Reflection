package com.astriex.reflection.presentation.activity.postNote

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.domain.use_case.note_use_case.NoteUseCases
import com.astriex.reflection.domain.use_case.user_use_case.UserUseCases
import com.astriex.reflection.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    userUseCases: UserUseCases
) :
    ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading
    val userData = userUseCases.getUserDataUseCase()
    var result = MutableLiveData<Result>()

    // Prevent repeat of login btn snackbar message on config change
    fun resetResult() {
        result = MutableLiveData<Result>()
    }

    suspend fun saveNote(title: String, content: String, imageUri: Uri?, username: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            result.value =
                noteUseCases.postNoteUseCase(title, content, imageUri, username)
            _isLoading.postValue(false)
        }
    }

}