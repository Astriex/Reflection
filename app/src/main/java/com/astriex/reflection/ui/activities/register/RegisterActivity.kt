package com.astriex.reflection.ui.activities.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityRegisterBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupActionbar()
        setupListeners()
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupListeners() {
        binding.btnCreateAccount.setOnClickListener {
            getFieldData()
            if (viewModel.isDataValid(username, email, password)) {
                register()
            } else {
                snackbar(viewModel.message)
            }
        }
    }

    private fun register() {
        viewModel.registerUser(email, password, username).observe(this, { result ->
            handleRegisterResponse(result)
            viewModel.resetResult()
        })
    }

    private fun handleRegisterResponse(result: Result) {
        when (result) {
            is Result.Success -> {
                showNotesList()
                finish()
            }
            is Result.Error -> {
                snackbar(result.message)
            }
        }
    }

    private fun showNotesList() {
        launchActivity<NotesListActivity>()
    }

    private fun getFieldData() {
        username = binding.etUsername.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        email = binding.actvEmail.text.toString().trim()
    }

}