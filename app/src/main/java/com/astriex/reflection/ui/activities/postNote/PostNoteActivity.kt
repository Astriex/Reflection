package com.astriex.reflection.ui.activities.postNote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityPostNoteBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostNoteBinding
    private lateinit var viewModel: PostNoteViewModel
    private val GALLERY_CODE = 1
    private var imageUri: Uri? = null
    private lateinit var title: String
    private lateinit var content: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_note)
        binding.lifecycleOwner = this

        viewModel =
            ViewModelProvider(this, PostNoteViewModelFactory(FirebaseRepository.getInstance())).get(
                PostNoteViewModel::class.java
            )
        binding.viewModel = viewModel

        setupViews()
    }

    private fun setupViews() {
        viewModel.userData.observe(this, Observer {
            binding.tvPostUsername.text = it.username
            username = it.username
        })
    }

    fun saveNote(view: View) {
        getFields()
        if (viewModel.isDataValid(title, content, imageUri)) {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.saveNote(title, content, imageUri, username)
            }
            viewModel.result.observe(this, Observer {
                handleResponse(it)
            })
        } else {
            Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startNotesListActivity() {
        startActivity(Intent(this, NotesListActivity::class.java))
        finish()
    }

    private fun getFields() {
        title = binding.etPostTitle.text.toString().trim()
        content = binding.etPostContent.text.toString().trim()
    }

    fun getImage(view: View) {
        startActivityForResult(
            Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*"), GALLERY_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.data
                binding.ivHeader.setImageURI(imageUri)
            }
        }
    }

    private fun handleResponse(data: Result) {
        when (data) {
            is Result.Success -> {
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                startNotesListActivity()
                finish()
            }
            is Result.Error -> {
                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}