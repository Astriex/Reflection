package com.astriex.reflection.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.astriex.reflection.domain.model.Note
import com.astriex.reflection.domain.model.User
import com.astriex.reflection.domain.repository.FirebaseRepository
import com.astriex.reflection.util.Constants
import com.astriex.reflection.util.Result
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    db: FirebaseFirestore,
    private val storageInstance: FirebaseStorage
): FirebaseRepository {
    private val userCollectionReference = db.collection(Constants.USERS_COLLECTION)
    private val storageReference = storageInstance.reference
    private val notebookCollectionReference = db.collection(Constants.NOTEBOOK_COLLECTION)

    private val _userData = MutableLiveData<User>()
    override var userData: LiveData<User> = _userData

    /**
     * Register a new app user.
     */
    override suspend fun registerUser(email: String, password: String, username: String): Result {
        return try {
            withContext(Dispatchers.IO) {
                createAccount(email, password)
                saveUserToCollection(username)
                Result.Success()
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    /**
     * Create a new user in Firestore using email and password.
     */
    override suspend fun createAccount(email: String, password: String): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    /**
     * Save new user to Firestore user collection.
     */
    override suspend fun saveUserToCollection(username: String): DocumentReference {
        return userCollectionReference.add(User(username, getCurrentUserId()!!)).await()
    }

    /**
     * Save new note.
     * Since the note is saved in two parts (background image - FirebaseStorage, note data - Firestore),
     * we have to save both parts separately.
     */
    override suspend fun saveNote(title: String, content: String, imageUri: Uri, username: String): Result {
        return try {
            withContext(Dispatchers.IO) {
                // Save image
                val filePath = storageReference
                    .child("notebook_images")
                    .child("my_image_${Timestamp.now().seconds}")
                filePath.putFile(imageUri).await()

                // Save note data
                val docRef = notebookCollectionReference.add(
                    Note(
                        title,
                        content,
                        filePath.downloadUrl.await().toString(),
                        getCurrentUserId(),
                        Timestamp.now(),
                        username
                    )
                ).await()

                Result.Success(docRef)
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    /**
     * Login user with email and password.
     */
    override suspend fun loginUser(email: String, password: String): Result {
        return try {
            withContext(Dispatchers.IO) {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Result.Success()
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    /**
     * Load user data for the current user.
     */
    override fun loadUserData() {
        getCurrentUserId()?.let { id ->
            var user: User? = null
            userCollectionReference
                .whereEqualTo("userId", id)
                .addSnapshotListener { value, _ ->
                    value?.forEach {
                        if (it.exists()) {
                            user = User(
                                it.getString(Constants.USERNAME)!!,
                                it.getString(Constants.USER_ID)!!
                            )
                            _userData.value = user!!
                        }
                    }
                }
        }
    }

    /**
     * Get all notes for the current user.
     */
    override fun getNotebookData() = flow<Result> {
        emit(Result.Loading)
        val snapshot =
            notebookCollectionReference
                .whereEqualTo(Constants.USER_ID, getCurrentUserId())
                .orderBy("timeAdded", Query.Direction.DESCENDING)
                .get().await()
        val notes = snapshot.toObjects(Note::class.java)
        emit(Result.Success(data = notes))
    }.catch {
        emit(Result.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Update note data.
     */
    override suspend fun updateNote(note: Note): Result {
        return try {
            withContext(Dispatchers.IO) {
                val query = notebookCollectionReference
                    .whereEqualTo("imageUrl", note.imageUrl)
                    .get().await()

                query.documents.forEach { document ->
                    document.reference.update(
                        mapOf(
                            "title" to note.title,
                            "content" to note.content,
                            "imageUrl" to note.imageUrl,
                            "userId" to note.userId,
                            "timeAdded" to Timestamp.now(),
                            "username" to note.username
                        )
                    ).await()
                }
                Result.Success()
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    /**
     * Delete a note.
     * Since the note is saved in two parts (background image - FirebaseStorage, note data - Firestore),
     * we have to delete both parts.
     */
    override suspend fun deleteNote(note: Note): Result {
        return try {
            withContext(Dispatchers.IO) {
                // Delete background image
                storageInstance.getReferenceFromUrl(note.imageUrl!!).delete().await()

                // Delete note data
                notebookCollectionReference
                    .whereEqualTo("imageUrl", note.imageUrl)
                    .get()
                    .await()
                    .documents.forEach { document ->
                        document.reference.delete()
                    }
                Result.Success()
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    /**
     * Sign out currently logged in firebase user.
     */
    override fun signOut() = firebaseAuth.signOut()

    /**
     * Get a currently logged in firebase user.
     */
    override fun getCurrentUser() = firebaseAuth.currentUser

    /**
     * Get a firebase user ID for a currently logged in user.
     */
    override fun getCurrentUserId() = getCurrentUser()?.uid

}