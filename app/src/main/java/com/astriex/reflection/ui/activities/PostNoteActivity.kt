package com.astriex.reflection.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.NotesApplication
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityPostNoteBinding
import com.astriex.reflection.data.repository.FirebaseRepository
import com.astriex.reflection.ui.viewModels.PostNoteViewModel
import com.astriex.reflection.ui.viewModels.PostNoteViewModelFactory

class PostNoteActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPostNoteBinding
    private lateinit var viewModel: PostNoteViewModel
    private val GALLERY_CODE = 1
    private var imageUri: Uri? = null
    private lateinit var title: String
    private lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_note)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, PostNoteViewModelFactory(FirebaseRepository())).get(
            PostNoteViewModel::class.java
        )

        setupListeners()
        setupViews()
    }

    private fun setupViews() {
        binding.tvPostUsername.text = NotesApplication.appUsername
    }

    private fun setupListeners() {
        binding.btnPostSave.setOnClickListener(this)
        binding.ivAddPhoto.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPostSave -> {
                getFields()
                savePost()
            }
            R.id.ivAddPhoto -> {
                getImage()
            }
        }
    }

    private fun savePost() {
        viewModel.savePost(title, content, imageUri)
    }

    private fun getFields() {
        title = binding.etPostTitle.text.toString().trim()
        content = binding.etPostContent.text.toString().trim()
    }

    private fun getImage() {
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