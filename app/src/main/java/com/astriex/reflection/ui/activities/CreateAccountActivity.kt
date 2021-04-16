package com.astriex.reflection.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityCreateAccountBinding
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModel
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            LoginRegisterViewModelFactory(FirebaseRepository())
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
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.registerUser(email, password, username)
        }
        startPostNoteActivity()
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

}