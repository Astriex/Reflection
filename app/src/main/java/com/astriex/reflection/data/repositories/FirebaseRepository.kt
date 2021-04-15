package com.astriex.reflection.data.repositories

import android.net.Uri
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.models.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class FirebaseRepository() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userCollectionReference = db.collection("Users")
    private val storageReference = FirebaseStorage.getInstance().reference
    private val notebookCollectionReference = db.collection("Notebook")

    suspend fun registerUser(email: String, password: String, username: String) {
        createAcc(email, password)
        saveUserToFirestore(username)
    }

    suspend fun saveUserToFirestore(username: String) {
        userCollectionReference.add(User(username, firebaseAuth.currentUser!!.uid)).await()
    }

    suspend fun createAcc(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun saveNote(title: String, content: String, imageUri: Uri, username: String) {
        val filePath = uploadImageToStorage(imageUri)
        saveNoteToFirestore(title, content, filePath, username)
    }

    private suspend fun saveNoteToFirestore(
        title: String,
        content: String,
        filePath: StorageReference,
        username: String
    ) {
        notebookCollectionReference.add(
            Note(
                title,
                content,
                filePath.downloadUrl.await().toString(),
                firebaseAuth.currentUser!!.uid,
                Timestamp.now(),
                username
            )
        ).await()
    }

    private suspend fun uploadImageToStorage(imageUri: Uri): StorageReference {
        val filePath = storageReference
            .child("notebook_images")
            .child("my_image_${Timestamp.now().seconds}")
        filePath.putFile(imageUri).await()
        return filePath
    }

}