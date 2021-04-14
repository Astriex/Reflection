package com.astriex.reflection.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityPostNoteBinding
import com.astriex.reflection.repository.FirebaseRepository
import com.astriex.reflection.ui.viewModels.PostNoteViewModel
import com.astriex.reflection.ui.viewModels.PostNoteViewModelFactory

class PostNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostNoteBinding
    private lateinit var viewModel: PostNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_note)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, PostNoteViewModelFactory(FirebaseRepository())).get(
            PostNoteViewModel::class.java
        )

    }
}