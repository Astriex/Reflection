package com.astriex.reflection.ui.activities.editNote

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.astriex.reflection.R
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.databinding.ActivityEditNoteBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.isConnected
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.toast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

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
                if (viewModel.isDataValid(receivedNote!!.title!!, receivedNote!!.content!!)) {
                    update()
                }
            } else {
                toast(getString(R.string.no_network_warning))
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