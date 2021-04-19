package com.astriex.reflection.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.data.repositories.FirebaseRepository
import com.astriex.reflection.databinding.ActivityLoginBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.ui.activities.register.CreateAccountActivity
import com.astriex.reflection.util.Result

class LoginActivity : AppCompatActivity() {
    private lateinit var bindingLogin: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogin = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindingLogin.lifecycleOwner = this

        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(FirebaseRepository.getInstance())
        ).get(
            LoginViewModel::class.java
        )
        bindingLogin.viewModel = viewModel

        setupActionbar()
        setupListeners()
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupListeners() {
        bindingLogin.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }
        bindingLogin.btnLogin.setOnClickListener {
            getFields()
            if (viewModel.isDataValid(email, password)) {
                login()
            } else {
                Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login() {
        viewModel.loginUser(email, password).observe(this, Observer { result ->
            handleResult(result)
            viewModel.resetResponse()
        })
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> {
                startActivity(Intent(this, NotesListActivity::class.java))
                finish()
            }
            is Result.Error -> {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFields() {
        email = bindingLogin.actEmail.text.toString().trim()
        password = bindingLogin.etPassword.text.toString().trim()
    }
}