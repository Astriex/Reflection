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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        setupListeners()
    }

    private fun setupListeners() {
        bindingLogin.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }
        bindingLogin.btnLogin.setOnClickListener {
            getFields()
            if (viewModel.isDataValid(email, password)) {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.loginUser(email, password)
                }
                viewModel.result.observe(this, Observer {
                    handleResult(it)
                })
            } else {
                Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleResult(data: Result) {
        when (data) {
            is Result.Success -> {
                startActivity(Intent(this, NotesListActivity::class.java))
                finish()
            }
            is Result.Error -> {
                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFields() {
        email = bindingLogin.actEmail.text.toString().trim()
        password = bindingLogin.etPassword.text.toString().trim()
    }
}