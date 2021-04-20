package com.astriex.reflection.util

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.astriex.reflection.data.models.Note

fun Activity.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

inline fun <reified T : Any> Activity.launchActivity(
    key: String? = null, value: Note? = null
) {
    val intent = Intent(this, T::class.java)
    if (value != null) {
        intent.putExtra(key, value)
        startActivity(intent)
    } else {
        startActivity(intent)
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

