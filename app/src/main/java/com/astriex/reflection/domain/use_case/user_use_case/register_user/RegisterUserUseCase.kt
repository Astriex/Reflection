package com.astriex.reflection.domain.use_case.user_use_case.register_user

import com.astriex.reflection.domain.repository.FirebaseRepository
import com.astriex.reflection.util.Result
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(val repository: FirebaseRepository) {
    suspend operator fun invoke(email: String, password: String, username: String): Result {
        return if (areFieldsEmpty(email, password, username)) {
            repository.registerUser(email, password, username)
        } else {
            Result.Error("Fields cannot be empty")
        }
    }

    private fun areFieldsEmpty(email: String, password: String, username: String) =
        email.trim().isNotEmpty() && password.trim().isNotEmpty() && username.trim().isNotEmpty()
}