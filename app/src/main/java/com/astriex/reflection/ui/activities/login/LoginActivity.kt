package com.astriex.reflection.ui.activities.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityLoginBinding
import com.astriex.reflection.ui.activities.notesList.NotesListActivity
import com.astriex.reflection.ui.activities.register.RegisterActivity
import com.astriex.reflection.util.Result
import com.astriex.reflection.util.launchActivity
import com.astriex.reflection.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var bindingLogin: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogin = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindingLogin.lifecycleOwner = this

        bindingLogin.viewModel = viewModel

        setupActionbar()
        setupListeners()
    }

    private fun setupActionbar() {
        supportActionBar!!.elevation = 0F
    }

    private fun setupListeners() {
        bindingLogin.btnCreateAccount.setOnClickListener {
            launchActivity<RegisterActivity>()
        }
        bindingLogin.btnLogin.setOnClickListener {
            getFieldData()
            if (viewModel.isDataValid(email, password)) {
                login()
            } else {
                toast(viewModel.message)
            }
        }
    }

    private fun login() {
        viewModel.loginUser(email, password).observe(this, { result ->
            handleResult(result)
            viewModel.resetResult()
        })
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> {
                launchActivity<NotesListActivity>()
                finish()
            }
            is Result.Error -> {
                toast(result.message)
            }
        }
    }

    private fun getFieldData() {
        email = bindingLogin.actEmail.text.toString().trim()
        password = bindingLogin.etPassword.text.toString().trim()
    }
}