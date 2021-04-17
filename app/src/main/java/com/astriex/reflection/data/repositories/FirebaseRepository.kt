package com.astriex.reflection.data.repositories

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.models.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userCollectionReference = db.collection("Users")
    private val storageReference = FirebaseStorage.getInstance().reference
    private val notebookCollectionReference = db.collection("Notebook")

    val userData = MutableLiveData<User>()

    companion object {
        @Volatile
        private var instance: FirebaseRepository? = null
        fun getInstance(): FirebaseRepository {
            return instance ?: FirebaseRepository().also { instance = it }
        }
    }

    suspend fun registerUser(email: String, password: String, username: String) {
        withContext(Dispatchers.IO) {
            createAcc(email, password)
            saveUserToFirestore(username)

        }
        getUserData()
    }

    suspend fun saveUserToFirestore(username: String) {
        userCollectionReference.add(User(username, firebaseAuth.currentUser!!.uid)).await()
    }

    suspend fun createAcc(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun saveNote(title: String, content: String, imageUri: Uri, username: String) {
        withContext(Dispatchers.IO) {
            val filePath = storageReference
                .child("notebook_images")
                .child("my_image_${Timestamp.now().seconds}")
            filePath.putFile(imageUri).await()

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
    }

    suspend fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        getUserData()
    }

    private fun getUserData(): User? {
        var user: User? = null
        firebaseAuth.currentUser?.let {
            val userId = firebaseAuth.currentUser!!.uid
            userCollectionReference
                .whereEqualTo("userId", userId)
                .addSnapshotListener { value, _ ->
                    value?.forEach {
                        if (it.exists()) {
                            user = User(it.getString("username")!!, it.getString("userId")!!)
                            userData.postValue(user!!)
                        }
                    }
                }
        }
        return user
    }

}