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

    private val _currentUsername = MutableLiveData<String>("")
    val currentUsername: LiveData<String>
        get() = _currentUsername
    private val _registrationMessage = MutableLiveData<String>("")
    val registrationMessage: LiveData<String>
        get() = _registrationMessage

    // progress indicator
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun registerUser(email: String, password: String, username: String): LiveData<Boolean> {
        _isLoading.postValue(true)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUsername.postValue(username)
                    val currentUserId = firebaseAuth.currentUser!!.uid

                    // create user object to create User collection
                    val userObj = HashMap<String, String>()
                    userObj.put("userId", currentUserId)
                    userObj.put("username", username)

                    // save to firestore
                    collectionReference.add(userObj)
                        .addOnSuccessListener { documentReference ->
                            documentReference.get()
                                .addOnCompleteListener {
                                    if(it.result!!.exists()) {
                                        _isLoading.postValue(false)
                                        _isSuccessfulRegistration.postValue(true)
                                    } else {
                                        _isLoading.postValue(false)
                                    }
                                }
                        }
                        .addOnFailureListener { }
                }
                _isLoading.postValue(false)
            }
            .addOnFailureListener {
                _isLoading.postValue(false)
                _registrationMessage.postValue("")
            }
        return _isSuccessfulRegistration
    }

}