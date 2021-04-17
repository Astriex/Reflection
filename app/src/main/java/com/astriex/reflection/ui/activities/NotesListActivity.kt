package com.astriex.reflection.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityNotesListBinding
import com.astriex.reflection.ui.viewModels.NotesListViewModel
import com.astriex.reflection.ui.viewModels.NotesListViewModelFactory

class NotesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesListBinding
    private lateinit var viewModel: NotesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            NotesListViewModelFactory(FirebaseRepository.getInstance())
        ).get(NotesListViewModel::class.java)
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

    override fun onStart() {
        super.onStart()

    }
}