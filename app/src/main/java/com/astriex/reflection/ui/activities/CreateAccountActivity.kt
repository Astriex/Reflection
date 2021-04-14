package com.astriex.reflection.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityCreateAccountBinding
import com.astriex.reflection.repository.FirebaseRepository
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModel
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModelFactory

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var viewModel: LoginRegisterViewModel
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(
            this,
            LoginRegisterViewModelFactory(FirebaseRepository(application))
        ).get(
            LoginRegisterViewModel::class.java
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
        viewModel.registerUser(email, password, username).observe(this, Observer {
            if(it != null) startPostNoteActivity() else showRegistrationFailedMessage()
        })
    }

    private fun startPostNoteActivity() {
        startActivity(
            Intent(this, PostNoteActivity::class.java)
                .putExtra("username", username)
        )
        finish()
    }

    private fun getFieldData() {
        username = binding.etUsername.text.toString()
        password = binding.etPassword.text.toString()
        email = binding.actvEmail.text.toString()
    }

    private fun showRegistrationFailedMessage() {
        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
    }

}