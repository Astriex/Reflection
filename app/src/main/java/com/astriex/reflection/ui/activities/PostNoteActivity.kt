package com.astriex.reflection.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.astriex.reflection.R
import com.astriex.reflection.databinding.ActivityPostNoteBinding

class PostNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_note)
        binding.lifecycleOwner = this

        val username = intent.getBundleExtra("username")
    }
}