package com.astriex.reflection.ui.viewModels

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.repository.FirebaseRepository

class PostNoteViewModel(private val repository: FirebaseRepository): ViewModel() {
    val isLoading: LiveData<Boolean>
        get() = repository.isLoading

    fun savePost(title: String, content: String, imageUri: Uri?) {
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && imageUri != null) {
            repository.savePost(title, content, imageUri)
        } else {

        }
    }

}