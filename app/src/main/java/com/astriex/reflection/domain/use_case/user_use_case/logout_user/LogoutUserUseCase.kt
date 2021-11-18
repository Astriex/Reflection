package com.astriex.reflection.domain.use_case.user_use_case.logout_user

import com.astriex.reflection.domain.repository.FirebaseRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(private val repository: FirebaseRepository) {

    operator fun invoke() {
        repository.signOut()
    }
}