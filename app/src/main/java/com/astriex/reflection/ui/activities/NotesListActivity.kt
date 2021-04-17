package com.astriex.reflection.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityNotesListBinding

class NotesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
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
        finish()
    }

    private fun logout() {

    }
}