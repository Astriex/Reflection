package com.astriex.reflection.ui.viewModels

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.astriex.reflection.data.models.User
import com.astriex.reflection.data.repositories.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostNoteViewModel(private val repository: FirebaseRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val userData: LiveData<User> = repository.userData

    suspend fun saveNote(title: String, content: String, imageUri: Uri?, username: String) {
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && imageUri != null) {
            isLoading.postValue(true)
            withContext(Dispatchers.IO) {
                repository.saveNote(title, content, imageUri, username)
            }
            isLoading.postValue(false)
        }
    }

    fun signOut() {
        repository.signOut()
    }

}