package com.astriex.reflection.domain.use_case.user_use_case.login_user

import com.astriex.reflection.domain.repository.FirebaseRepository
import com.astriex.reflection.util.Result
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(val repository: FirebaseRepository) {

    suspend operator fun invoke(email: String, password: String): Result {
        return if (isDataValid(email, password)) {
            repository.loginUser(email, password)
        } else {
            Result.Error("Fields cannot be empty")
        }
    }

    private fun isDataValid(email: String, password: String) =
        email.trim().isNotEmpty() && password.trim().isNotEmpty()

}