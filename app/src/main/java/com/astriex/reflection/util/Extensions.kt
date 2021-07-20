package com.astriex.reflection.util

import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

fun Activity.isConnected(): Boolean {
    var status = false
    val conManager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (conManager != null && conManager.activeNetwork != null && conManager.getNetworkCapabilities(
                conManager.activeNetwork
            ) != null
        ) {
            status = true
        }
    } else {
        if (conManager.activeNetwork != null) {
            status = true
        }
    }
    return status
}

