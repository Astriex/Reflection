package com.astriex.reflection.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityLoginBinding
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModel
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var bindingLogin: ActivityLoginBinding
    private lateinit var viewModel: LoginRegisterViewModel
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogin = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindingLogin.lifecycleOwner = this

        viewModel = ViewModelProvider(
            this,
            LoginRegisterViewModelFactory(FirebaseRepository.getInstance())
        ).get(
            LoginRegisterViewModel::class.java
        )
        bindingLogin.viewModel = viewModel

        setupListeners()
    }

    private fun setupListeners() {
        bindingLogin.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }
        bindingLogin.btnLogin.setOnClickListener {
            getFields()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.loginUser(email, password)
            }
            startActivity(Intent(this, PostNoteActivity::class.java))
            finish()
        }
    }

    private fun getFields() {
        email = bindingLogin.actEmail.text.toString()
        password = bindingLogin.etPassword.text.toString()
    }
}