package com.astriex.reflection.util


object UserData {

    var appUsername: String = String()
    var appUserId: String = String()

    fun setUsername(newUsername: String) {
        appUsername = newUsername
    }

    fun getUsername() = appUsername

    fun setUserId(newUserId: String) {
        appUserId = newUserId
    }

}