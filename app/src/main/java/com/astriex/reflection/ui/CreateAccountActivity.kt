package com.astriex.reflection.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityMainBinding
import com.astriex.reflection.repository.FirebaseRepository
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModel
import com.astriex.reflection.ui.viewModels.LoginRegisterViewModelFactory

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var bindingCreateAccount: ActivityMainBinding
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingCreateAccount = DataBindingUtil.setContentView(this, R.layout.activity_create_account)

        viewModel = ViewModelProvider(
            this,
            LoginRegisterViewModelFactory(FirebaseRepository(application))
        ).get(
            LoginRegisterViewModel::class.java
        )

        setupListeners()
    }

    private fun setupListeners() {

    }
}