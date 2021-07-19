package com.astriex.reflection.ui.activities.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityRegisterBinding
import com.astriex.reflection.ui.activities.postNote.PostNoteActivity
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.toast
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
                toast(viewModel.message.value!!)
            }
        }
    }

    private fun register() {
        viewModel.registerUser(email, password, username).observe(this, { result ->
            handleResponse(result)
            viewModel.resetResult()
        })
    }

    private fun handleResponse(result: Result) {
        when (result) {
            is Result.Success -> {
                toast("Account created successfully")
                startNewNote()
            }
            is Result.Error -> {
                toast(result.message)
            }
        }
    }

    private fun startNewNote() {
        launchActivity<PostNoteActivity>()
        finish()
    }

    private fun getFieldData() {
        username = binding.etUsername.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        email = binding.actvEmail.text.toString().trim()
    }

}