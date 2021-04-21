package com.astriex.reflection.ui.activities.postNote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityPostNoteBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.Constants.Companion.GALLERY_CODE
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.isConnected
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostNoteBinding
    private lateinit var viewModel: PostNoteViewModel
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
        if(isConnected()) {
            getFields()
            if (viewModel.isDataValid(title, content, imageUri)) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.saveNote(title, content, imageUri, username)
                }
                viewModel.result.observe(this, {
                    handleResponse(it)
                })
            } else {
                toast(viewModel.message)
            }
        } else {
            toast(getString(R.string.no_network_warning))
        }
    }

    private fun startNotesListActivity() {
        launchActivity<NotesListActivity>()
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

    private fun handleResponse(result: Result) {
        when (result) {
            is Result.Success -> {
                startNotesListActivity()
                finish()
            }
            is Result.Error -> {
                toast(result.message)
            }
        }
    }
}