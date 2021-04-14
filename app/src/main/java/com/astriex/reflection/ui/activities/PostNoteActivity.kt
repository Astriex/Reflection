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
        setContentView(R.layout.activity_post_note)

    }
}