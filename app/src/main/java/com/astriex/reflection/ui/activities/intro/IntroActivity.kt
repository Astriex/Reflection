package com.astriex.reflection.ui.activities.intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityIntroBinding
import com.astriex.reflection.ui.activities.login.LoginActivity
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var viewModel: IntroViewModel

    @Inject
    lateinit var repository: FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, IntroViewModelFactory(repository)).get(
                IntroViewModel::class.java
            )

        setupActionbar()
        setupListeners()
        userLoggedInCheck()
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupListeners() {
        binding.btnStart.setOnClickListener {
            launchActivity<LoginActivity>()
            finish()
        }
    }

    private fun userLoggedInCheck() {
        if (viewModel.getCurrentUser() != null) {
            launchActivity<NotesListActivity>()
            finish()
        }
    }
}