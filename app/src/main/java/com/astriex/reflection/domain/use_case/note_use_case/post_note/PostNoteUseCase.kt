package com.astriex.reflection.domain.use_case.note_use_case.post_note

import android.net.Uri
import com.astriex.reflection.domain.repository.FirebaseRepository
import com.astriex.reflection.util.Result
import javax.inject.Inject

class PostNoteUseCase @Inject constructor(val repository: FirebaseRepository) {

    suspend operator fun invoke(title: String, content: String, imageUri: Uri?, username: String): Result {
        return when {
            isDataValid(title, content, imageUri) -> {
                repository.saveNote(title, content, imageUri!!, username)
            }
            imageUri == null -> {
                Result.Error("Please select a photo")
            }
            else -> {
                Result.Error("Fields cannot be empty")
            }
        }
    }



    private fun isDataValid(title: String, content: String, imageUri: Uri?) =
        title.trim().isNotEmpty() && content.trim().isNotEmpty() && (imageUri != null)
}