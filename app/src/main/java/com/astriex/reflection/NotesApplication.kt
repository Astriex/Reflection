package com.astriex.reflection

import android.app.Application

class NotesApplication() : Application() {

    init {
        appUsername = String()
        appUserId = String()
    }

    companion object {

        var appUsername: String? = null
        var appUserId: String? = null

    }

}