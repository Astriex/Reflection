package com.astriex.reflection.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.astriex.reflection.NotesApplication
import com.astriex.reflection.data.models.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseRepository() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userCollectionReference: CollectionReference = db.collection("Users")
    private var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private var notebookCollectionReference: CollectionReference = db.collection("Notebook")

    // user auth and db preparations done flag
    private val _isSuccessfulRegistration = MutableLiveData<Boolean>(false)

    // progress indicator
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun registerUser(email: String, password: String, username: String): LiveData<Boolean> {
        _isLoading.postValue(true)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setupFirestore(username)
                }
                _isLoading.postValue(false)
            }
            .addOnFailureListener {
                _isLoading.postValue(false)
            }
        return _isSuccessfulRegistration
    }

    private fun setupFirestore(username: String) {
        val currentUserId = firebaseAuth.currentUser!!.uid

        // create user object to save to User collection
        //val userObj = HashMap<String, String>()
        //userObj.put("userId", currentUserId)
        //userObj.put("username", username)

        val user = User(username, currentUserId)

        // save data to app class to avoid unnecessary db calls
        NotesApplication.appUsername = username
        NotesApplication.appUserId = currentUserId

        // save document to firestore
        userCollectionReference.add(user)
            .addOnSuccessListener { documentReference ->
                documentReference.get()
                    .addOnCompleteListener {
                        if (it.result!!.exists()) {
                            _isLoading.postValue(false)
                            _isSuccessfulRegistration.postValue(true)
                        } else {
                            _isLoading.postValue(false)
                        }
                    }
            }
            .addOnFailureListener { }
    }

    fun savePost(title: String, content: String, imageUri: Uri) {
        _isLoading.postValue(true)

        val filepath: StorageReference = storageReference
            .child("notebook_images")
            .child("my_image_" + Timestamp.now().seconds)

        filepath.putFile(imageUri)
            .addOnSuccessListener {
                _isLoading.postValue(false)
                // TODO: create Notebook object
                // TODO: invoke collectionReference
                // TODO: save Notebook instance

            }
            .addOnFailureListener{
                _isLoading.postValue(false)
            }
    }

}