package com.astriex.reflection.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

inline fun <reified T : Any> newIntent(context: Context) = Intent(context, T::class.java)

inline fun <reified T : Any> Context.launchActivity(noinline init: Intent.() -> Unit = {}, extras: Bundle? = null, ) {
    val intent = newIntent<T>(this)
    intent.init()
    if(extras != null) {
        startActivity(intent, extras)
    } else {
        startActivity(intent)
    }
}

