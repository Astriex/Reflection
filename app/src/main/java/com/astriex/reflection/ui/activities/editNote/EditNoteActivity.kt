package com.astriex.reflection.ui.activities.editNote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityEditNoteBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.toast
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var viewModel: EditNoteViewModel
    private var receivedNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note)

        viewModel =
            ViewModelProvider(this, EditNoteViewModelFactory(FirebaseRepository.getInstance())).get(
                EditNoteViewModel::class.java
            )
        binding.viewModel = viewModel

        setupEditNoteView()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnPostSave.setOnClickListener {
            getFields()
            if (viewModel.isDataValid(receivedNote!!.title!!, receivedNote!!.content!!)) {
                update()
            }
        }
    }

    private fun update() {
        viewModel.updateNote(receivedNote!!).observe(this, {
            handleResponse(it)
            viewModel.resetResult()
        })
    }

    private fun handleResponse(result: Result) {
        when (result) {
            is Result.Success -> launchActivity<NotesListActivity>()
            is Result.Error -> toast(result.message)
        }
    }

    private fun setupEditNoteView() {
        receivedNote = intent.getParcelableExtra("note") as? Note
        binding.let {
            it.tvEditUsername.text = receivedNote!!.username
            it.tvEditDate.text = getFormatedDate()
            it.etEditTitle.setText(receivedNote!!.title)
            it.etEditContent.setText(receivedNote!!.content)
            Glide.with(this).load(receivedNote!!.imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(it.ivEditHeader)
        }
    }

    private fun getFormatedDate(): String {
        val date = receivedNote!!.timeAdded!!.toDate()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return sdf.format(date)
    }

    private fun getFields() {
        receivedNote!!.title = binding.etEditTitle.text.toString()
        receivedNote!!.content = binding.etEditContent.text.toString()
    }
}