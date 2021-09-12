package com.astriex.reflection.ui.activities.postNote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.coroutineScope
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityPostNoteBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.Constants.Companion.GALLERY_CODE
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.isConnected
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostNoteBinding
    private val viewModel by viewModels<PostNoteViewModel>()
    private var imageUri: Uri? = null
    private lateinit var title: String
    private lateinit var content: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_note)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupActionbar()
        setupViews()
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupViews() {
        viewModel.userData.observe(this, {
            binding.tvPostUsername.text = it.username
            username = it.username
        })
    }

    fun saveNote(view: View) {
        if (isConnected()) {
            getFields()
            if (viewModel.isDataValid(title, content, imageUri)) {
                lifecycle.coroutineScope.launch {
                    viewModel.saveNote(title, content, imageUri, username)
                }
                viewModel.result.observe(this, {
                    handleSaveResponse(it)
                })
            } else {
                snackbar(viewModel.message)
            }
        } else {
            snackbar(getString(R.string.no_network_warning))
        }
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

    private fun handleSaveResponse(result: Result) {
        when (result) {
            is Result.Success -> {
                startNotesListActivity()
                finish()
            }
            is Result.Error -> {
                snackbar(result.message)
            }
        }
    }

    private fun startNotesListActivity() {
        launchActivity<NotesListActivity>()
    }

}