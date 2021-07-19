package com.astriex.reflection.data.repositories

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.data.models.User
import com.astriex.reflection.util.Constants
import com.astriex.reflection.util.Result
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    db: FirebaseFirestore,
    private val storageInstance: FirebaseStorage
) {
    private val userCollectionReference = db.collection(Constants.USERS_COLLECTION)
    private val storageReference = storageInstance.reference
    private val notebookCollectionReference = db.collection(Constants.NOTEBOOK_COLLECTION)

    val userData = MutableLiveData<User>()

    suspend fun registerUser(email: String, password: String, username: String): Result {
        return try {
            createAcc(email, password)
            saveUserToCollection(username)
            Result.Success()
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    private suspend fun createAcc(email: String, password: String): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    private suspend fun saveUserToCollection(username: String): DocumentReference {
        return userCollectionReference.add(User(username, firebaseAuth.currentUser!!.uid)).await()
    }

    suspend fun saveNote(title: String, content: String, imageUri: Uri, username: String): Result {
        return try {
            // save image
            val filePath = storageReference
                .child("notebook_images")
                .child("my_image_${Timestamp.now().seconds}")
            filePath.putFile(imageUri).await()

            // save note
            val docRef = notebookCollectionReference.add(
                Note(
                    title,
                    content,
                    filePath.downloadUrl.await().toString(),
                    firebaseAuth.currentUser!!.uid,
                    Timestamp.now(),
                    username
                )
            ).await()

            Result.Success(docRef)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun loginUser(email: String, password: String): Result {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.Success()
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    fun loadUserData() {
        var user: User? = null
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            userCollectionReference
                .whereEqualTo("userId", userId)
                .addSnapshotListener { value, _ ->
                    value?.forEach {
                        if (it.exists()) {
                            user = User(
                                it.getString(Constants.USERNAME)!!,
                                it.getString(Constants.USER_ID)!!
                            )
                            userData.postValue(user!!)
                        }
                    }
                }
        }

    }

    fun loadNotebookData() = flow<Result> {
        val currentUser = firebaseAuth.currentUser!!.uid
        val snapshot =
            notebookCollectionReference
                .whereEqualTo(Constants.USER_ID, currentUser)
                .orderBy("timeAdded", Query.Direction.DESCENDING)
                .get().await()
        val notes = snapshot.toObjects(Note::class.java)
        emit(Result.Success(data = notes))
    }.catch {
        emit(Result.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun updateNote(note: Note): Result {
        return try {
            val query = notebookCollectionReference
                .whereEqualTo("imageUrl", note.imageUrl)
                .get().await()

            query.documents.forEach {
                it.reference.update(
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
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun deleteNote(note: Note): Result {
        return try {
            storageInstance.getReferenceFromUrl(note.imageUrl!!).delete().await()

            notebookCollectionReference
                .whereEqualTo("imageUrl", note.imageUrl)
                .get()
                .await()
                .documents.forEach {
                    it.reference.delete()
                }
            Result.Success()
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }

    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        loadUserData()
        return firebaseAuth.currentUser
    }

}