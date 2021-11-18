package com.astriex.reflection.domain.use_case.note_use_case.edit_note

import android.annotation.SuppressLint
import com.astriex.reflection.domain.model.Note
import com.astriex.reflection.domain.repository.FirebaseRepository
import com.astriex.reflection.util.Result
import java.text.SimpleDateFormat
import javax.inject.Inject

class EditNoteUseCase @Inject constructor(val repository: FirebaseRepository) {

    suspend operator fun invoke(note: Note): Result {
        return when {
            isDataValid(note.title!!, note.content!!) -> {
                repository.updateNote(note)
            }
            else -> {
                Result.Error("Fields cannot be empty")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedDate(note: Note): String {
        val date = note.timeAdded!!.toDate()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return sdf.format(date)
    }

    private fun isDataValid(title: String, content: String) =
        title.trim().isNotEmpty() && content.trim().isNotEmpty()
}