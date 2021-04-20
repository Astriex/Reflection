package com.astriex.reflection.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

fun Activity.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

inline fun <reified T : Any> Activity.launchActivity(
    extras: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    if (extras != null) {
        startActivity(intent, extras)
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

