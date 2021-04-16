package com.astriex.reflection.ui.viewModels

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.reflection.data.repositories.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostNoteViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    suspend fun saveNote(title: String, content: String, imageUri: Uri?, username: String) {
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && imageUri != null) {
            isLoading.postValue(true)
            withContext(Dispatchers.IO) {
                repository.saveNote(title, content, imageUri, username)
            }
            isLoading.postValue(false)
        }
    }

}