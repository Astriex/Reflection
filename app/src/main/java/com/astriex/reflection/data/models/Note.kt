package com.astriex.reflection.data.models

import com.google.firebase.Timestamp

data class Note(
    val title: String,
    val content: String,
    val imageUri: String,
    val userId: String,
    val timeAdded: Timestamp,
    val username: String
)