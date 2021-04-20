package com.astriex.reflection.ui.activities.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityCreateAccountBinding
import com.astriex.reflection.ui.activities.postNote.PostNoteActivity
import com.astriex.reflection.util.Result

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(
            this,
            RegisterViewModelFactory(FirebaseRepository.getInstance())
        ).get(
            RegisterViewModel::class.java
        )
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
                registration()
            } else {
                Toast.makeText(this, viewModel.message.value, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registration() {
        viewModel.registerUser(email, password, username).observe(this, {
            handleResponse(it)
            viewModel.resetResult()
        })
    }

    private fun handleResponse(result: Result) {
        when (result) {
            is Result.Success -> {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
                startNewNote()
            }
            is Result.Error -> {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startNewNote() {
        startActivity(
            Intent(this, PostNoteActivity::class.java)
        )
        finish()
    }

    private fun getFieldData() {
        username = binding.etUsername.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        email = binding.actvEmail.text.toString().trim()
    }

}