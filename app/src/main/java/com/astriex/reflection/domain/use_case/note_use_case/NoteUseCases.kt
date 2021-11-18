package com.astriex.reflection.domain.use_case.note_use_case

import com.astriex.reflection.domain.use_case.note_use_case.delete_note.DeleteNoteUseCase
import com.astriex.reflection.domain.use_case.note_use_case.post_note.PostNoteUseCase
import com.astriex.reflection.domain.use_case.note_use_case.edit_note.EditNoteUseCase
import com.astriex.reflection.domain.use_case.note_use_case.load_notes_list.LoadNotesListUseCase

data class NoteUseCases(
    val postNoteUseCase: PostNoteUseCase,
    val editNoteUseCase: EditNoteUseCase,
    val loadNotesListUseCase: LoadNotesListUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase
)