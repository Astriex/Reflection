package com.astriex.reflection.ui.activities.notesList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.adapters.NoteListAdapter
import com.astriex.reflection.adapters.OnItemClickListener
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityNotesListBinding
import com.astriex.reflection.ui.activities.editNote.EditNoteActivity
import com.astriex.reflection.ui.activities.login.LoginActivity
import com.astriex.reflection.ui.activities.postNote.PostNoteActivity
import com.astriex.reflection.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotesListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding: ActivityNotesListBinding
    private lateinit var viewModel: NotesListViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes_list)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(
            this,
            NotesListViewModelFactory(FirebaseRepository.getInstance())
        ).get(NotesListViewModel::class.java)
        binding.viewModel = viewModel

        setupActionbar()
        setupViews()

        CoroutineScope(Dispatchers.Main).launch {
            loadNotes()
        }

    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupViews() {
        setNoNotesView()
    }

    private suspend fun loadNotes() {
        viewModel.loadNotes().collect { result ->
            when (result) {
                is Result.Success -> {
                    handleResponse(result)
                }
                is Result.Error -> {
                    binding.tvNoNotes.text = result.message
                }
            }
        }
    }

    private fun handleResponse(result: Result) {
        when (result) {
            is Result.Success -> {
                setNotesView()
                setupNotesList(result.data as List<Note>)
            }
            is Result.Error -> {
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                setNoNotesView()
            }
        }
    }

    private fun setNotesView() {
        binding.rvNotes.visibility = View.VISIBLE
        binding.tvNoNotes.visibility = View.INVISIBLE
    }

    private fun setNoNotesView() {
        binding.rvNotes.visibility = View.INVISIBLE
        binding.tvNoNotes.visibility = View.VISIBLE
    }

    private fun setupNotesList(notes: List<Note>) {
        adapter = NoteListAdapter(this, this)
        binding.rvNotes.adapter = adapter
        adapter.setNotes(notes)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionAdd -> {
                startNewNote()
            }
            R.id.actionSignOut -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startNewNote() {
        startActivity(Intent(this, PostNoteActivity::class.java))
        //finish()
    }

    private fun logout() {
        viewModel.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onitemClick(note: Note) {
        startActivity(Intent(this, EditNoteActivity::class.java).putExtra("note", note))
    }

}