package com.astriex.reflection.domain.use_case.user_use_case.is_user_logged_in

import com.astriex.reflection.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(private val repository: FirebaseRepository) {
    operator fun invoke(): FirebaseUser? = repository.getCurrentUser()
}