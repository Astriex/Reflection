package com.astriex.reflection.ui.activities.editNote

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityEditNoteBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.Result
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            if(viewModel.isDataValid(receivedNote!!.title!!, receivedNote!!.content!!)) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.updateNote(receivedNote!!)
                }
                viewModel.result.observe(this, Observer {
                    handleResponse(it)
                })
            }
            viewModel.updateNote(receivedNote!!)
        }
    }

    private fun handleResponse(result: Result) {
        when(result) {
            is Result.Success -> {
                startActivity(Intent(this, NotesListActivity::class.java))
            }
            is Result.Error -> Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupEditNoteView() {
        receivedNote = intent.getParcelableExtra("note") as? Note
        binding.let {
            tvEditUsername.text = receivedNote!!.username
            tvEditDate.text = receivedNote!!.timeAdded!!.toDate().toString()
            etEditTitle.setText(receivedNote!!.title)
            etEditContent.setText(receivedNote!!.content)
            Glide.with(this).load(receivedNote!!.imageUrl).placeholder(R.drawable.ic_image_placeholder)
                .into(ivEditHeader)
        }
    }

    private fun getFields() {
        receivedNote!!.title = binding.etEditTitle.text.toString()
        receivedNote!!.content = binding.etEditContent.text.toString()
    }
}