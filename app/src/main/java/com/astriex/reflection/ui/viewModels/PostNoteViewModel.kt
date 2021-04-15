package com.astriex.reflection.ui.viewModels

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.repository.FirebaseRepository

class PostNoteViewModel(private val repository: FirebaseRepository): ViewModel() {
    val isLoading: LiveData<Boolean>
        get() = repository.isLoading
    private lateinit var saveNoteDone: LiveData<Boolean>

    fun savePost(title: String, content: String, imageUri: Uri?, username: String): LiveData<Boolean> {
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && imageUri != null) {
            saveNoteDone = repository.savePost(title, content, imageUri, username)
        }
        return saveNoteDone
    }

}