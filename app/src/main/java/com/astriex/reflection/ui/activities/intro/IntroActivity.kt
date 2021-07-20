package com.astriex.reflection.ui.activities.intro

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.astriex.reflection.databinding.ActivityIntroBinding
import com.astriex.reflection.ui.activities.login.LoginActivity
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.launchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private val viewModel by viewModels<IntroViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

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