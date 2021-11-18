package com.astriex.reflection.domain.use_case.note_use_case.load_notes_list

import com.astriex.reflection.domain.repository.FirebaseRepository
import com.astriex.reflection.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadNotesListUseCase @Inject constructor(val repository: FirebaseRepository) {

    operator fun invoke(): Flow<Result> {
        return repository.getNotebookData()
    }
}