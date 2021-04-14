package com.astriex.reflection.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository(private val application: Application) {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var currentUser: FirebaseUser? = null
    private var collectionReference: CollectionReference = db.collection("Users")

    // user auth and db preparations done flags
    private val _loggedIn = MutableLiveData<Boolean>(false)
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn
    private val _currentUsername = MutableLiveData<String>("")
    val currentUsername: LiveData<String>
        get() = _currentUsername

    // test
    val message = MutableLiveData<String>("No message")

    // progress bar indicator
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    suspend fun registerUser(email: String, password: String, username: String) {
        _isLoading.postValue(true)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.postValue(false)
                if (task.isSuccessful) {
                    message.postValue("user created")
                } else {
                    message.postValue("task not successful")
                }
            }
            .addOnFailureListener {
                _isLoading.postValue(false)
                message.postValue("user not created")
            }
    }


}