package com.astriex.reflection.presentation.activity.editNote

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.astriex.reflection.R
import com.astriex.reflection.domain.model.Note
import com.astriex.reflection.databinding.ActivityEditNoteBinding
import com.astriex.reflection.presentation.activity.notesList.NotesListActivity
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.isConnected
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.snackbar
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    private val viewModel by viewModels<EditNoteViewModel>()
    private var receivedNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note)
        binding.viewModel = viewModel

        setupActionbar()
        setupEditNoteView()
        setupListeners()
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupListeners() {
        binding.btnPostSave.setOnClickListener {
            if (isConnected()) {
                getFields()
                update()
            } else {
                snackbar(getString(R.string.no_network_warning))
            }
        }
    }

    private fun setupEditNoteView() {
        receivedNote = intent.getParcelableExtra("note") as? Note
        binding.let {
            it.tvEditUsername.text = receivedNote!!.username
            it.tvEditDate.text = viewModel.getFormattedDate(receivedNote!!)
            it.etEditTitle.setText(receivedNote!!.title)
            it.etEditContent.setText(receivedNote!!.content)
            Glide.with(this).load(receivedNote!!.imageUrl)
                .into(it.ivEditHeader)
        }
    }

    private fun update() {
        viewModel.updateNote(receivedNote!!).observe(this, { result ->
            handleUpdateResponse(result)
            viewModel.resetResult()
        })
    }

    private fun handleUpdateResponse(result: Result) {
        when (result) {
            is Result.Success -> {
                launchActivity<NotesListActivity>()
                finish()
            }
            is Result.Error -> snackbar(result.message)
        }
    }

    private fun getFields() {
        receivedNote!!.title = binding.etEditTitle.text.toString()
        receivedNote!!.content = binding.etEditContent.text.toString()
    }
}