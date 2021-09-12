package com.astriex.reflection.ui.activities.notesList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.astriex.reflection.R
import com.astriex.reflection.data.adapters.NoteListAdapter
import com.astriex.reflection.data.adapters.OnItemClickListener
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.databinding.ActivityNotesListBinding
import com.astriex.reflection.ui.activities.editNote.EditNoteActivity
import com.astriex.reflection.ui.activities.login.LoginActivity
import com.astriex.reflection.ui.activities.postNote.PostNoteActivity
import com.astriex.reflection.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding: ActivityNotesListBinding
    private lateinit var adapter: NoteListAdapter
    private val viewModel by viewModels<NotesListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes_list)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupActionbar()
        setupListeners()

        setNoNotesView()
        lifecycle.coroutineScope.launch {
            loadNotes()
        }
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            startNewNote()
        }
    }

    private fun startNewNote() {
        launchActivity<PostNoteActivity>()
    }

    private suspend fun loadNotes() {
        viewModel.loadNotes().collect { result ->
            when (result) {
                is Result.Success -> {
                    viewModel.stopLoading()
                    if((result.data as List<*>).isNotEmpty()) {
                        showNotesView(result)
                    } else {
                        setNoNotesView()
                    }
                }
                is Result.Error -> {
                    viewModel.stopLoading()
                    showNoNotesView(result)
                }
                Result.Loading -> {
                    binding.tvNoNotes.hide()
                    viewModel.startLoading()
                }
            }
        }
    }

    private fun showNoNotesView(result: Result.Error) {
        snackbar(result.message)
        setNoNotesView()
    }

    private fun showNotesView(result: Result.Success) {
        setNotesView()
        setupNotesList(result.data as List<Note>)
    }

    private fun setNotesView() {
        binding.rvNotes.show()
        binding.tvNoNotes.hide()
    }

    private fun setNoNotesView() {
        binding.rvNotes.hide()
        binding.tvNoNotes.show()
    }

    private fun setupNotesList(notes: List<Note>) {
        adapter = NoteListAdapter(this, this)
        binding.rvNotes.adapter = adapter
        adapter.setNotes(notes)

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvNotes)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSignOut -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        viewModel.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onItemClick(note: Note) {
        launchActivity<EditNoteActivity>("note", note)
    }

    private val itemTouchHelperCallback = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val note = adapter.adapterNotes[position]
            viewModel.deleteNote(note).observe(this@NotesListActivity, {
                handleDeleteResponse(it, position)
                viewModel.resetResult()
            })
        }
    }

    private fun handleDeleteResponse(result: Result, position: Int) {
        when (result) {
            is Result.Success -> {
                adapter.adapterNotes.removeAt(position)
                adapter.notifyItemRemoved(position)
                if(adapter.adapterNotes.isEmpty()) {
                    setNoNotesView()
                }
            }
            is Result.Error -> snackbar(result.message)
        }
    }
}