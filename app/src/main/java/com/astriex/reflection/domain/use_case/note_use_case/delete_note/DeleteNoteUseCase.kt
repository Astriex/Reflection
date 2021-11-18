package com.astriex.reflection.domain.use_case.note_use_case.delete_note

import com.astriex.reflection.domain.model.Note
import com.astriex.reflection.domain.repository.FirebaseRepository
import com.astriex.reflection.util.Result
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val repository: FirebaseRepository) {

    suspend operator fun invoke(note: Note): Result {
        return repository.deleteNote(note)
    }
}