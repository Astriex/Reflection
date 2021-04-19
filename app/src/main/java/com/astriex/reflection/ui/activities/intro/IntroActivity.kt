package com.astriex.reflection.ui.activities.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityIntroBinding
import com.astriex.reflection.ui.activities.login.LoginActivity
import com.astriex.reflection.ui.activities.notesList.NotesListActivity

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var viewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, IntroViewModelFactory(FirebaseRepository.getInstance())).get(
                IntroViewModel::class.java
            )

        setupActionbar()
        setupListeners()
        userLoggedInCheck()
    }

    private fun userLoggedInCheck() {
        if (viewModel.getCurrentUser() != null) {
            startActivity(Intent(this, NotesListActivity::class.java))
        }
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupListeners() {
        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}