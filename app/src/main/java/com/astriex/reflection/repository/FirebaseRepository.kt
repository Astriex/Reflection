package com.astriex.reflection.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository(private val application: Application) {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var collectionReference: CollectionReference = db.collection("Users")

    // user auth and db preparations done flags
    private val _isSuccessfulRegistration = MutableLiveData<Boolean>(false)
    val isSuccessfulRegistration: LiveData<Boolean>
            get() = _isSuccessfulRegistration
    private val _currentUsername = MutableLiveData<String>("")
    val currentUsername: LiveData<String>
        get() = _currentUsername

    // progress indicator
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    suspend fun registerUser(email: String, password: String, username: String) {
        _isLoading.postValue(true)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.postValue(false)
                _isSuccessfulRegistration.postValue(task.isSuccessful)
                _currentUsername.postValue(username)
            }
            .addOnFailureListener {
                _isLoading.postValue(false)
            }
    }

}