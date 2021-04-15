package com.astriex.reflection.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityPostNoteBinding
import com.astriex.reflection.ui.viewModels.PostNoteViewModel
import com.astriex.reflection.ui.viewModels.PostNoteViewModelFactory

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

        viewModel = ViewModelProvider(this, PostNoteViewModelFactory(FirebaseRepository())).get(
            PostNoteViewModel::class.java
        )
        binding.viewModel = viewModel

        intent.getStringExtra("username")?.let {
            username = it
        }

        setupViews()
    }

    private fun setupViews() {
        binding.tvPostUsername.text = username
    }

    fun saveNote(view: View) {
        getFields()
        if(imageUri != null) {
            viewModel.saveNote(title, content, imageUri, username)
            startNotesListActivity()
        } else {
            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startNotesListActivity() {
        startActivity(Intent(this, NotesListActivity::class.java))
        //finish()
    }

    private fun getFields() {
        title = binding.etPostTitle.text.toString()
        content = binding.etPostContent.text.toString()
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
}