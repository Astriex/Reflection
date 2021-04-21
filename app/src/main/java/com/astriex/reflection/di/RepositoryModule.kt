package com.astriex.reflection.di

import com.astriex.reflection.data.repositories.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideFirebaseRepository(
        firebaseAuth: FirebaseAuth,
        db: FirebaseFirestore,
        storage: FirebaseStorage
    ): FirebaseRepository {
        return FirebaseRepository(
            firebaseAuth, db, storage
        )
    }

}