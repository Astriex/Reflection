package com.astriex.reflection.ui.activities.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityCreateAccountBinding
import com.astriex.reflection.ui.activities.postNote.PostNoteActivity
import com.astriex.reflection.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnCreateAccount.setOnClickListener {
            getFieldData()
            userRegistration()
        }
    }

    private fun userRegistration() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.registerUser(email, password, username)
        }
        viewModel.result.observe(this, Observer {
            handleResponse(it)
        })
    }

    private fun handleResponse(data: Result) {
        when(data) {
            is Result.Success -> {
                if(data.data == true) {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
                    showNewNote()
                } else {
                    Toast.makeText(this, "Registration not successful, try again", Toast.LENGTH_LONG).show()
                }
            }
            is Result.Error -> {
                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showNewNote() {
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