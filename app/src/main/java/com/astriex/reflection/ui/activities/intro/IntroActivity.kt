package com.astriex.reflection.ui.activities.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.astriex.reflection.databinding.ActivityIntroBinding
import com.astriex.reflection.ui.activities.login.LoginActivity

class IntroActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        setupListeners()
    }

    private fun setupListeners() {
        bindingMain.btnStart.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}