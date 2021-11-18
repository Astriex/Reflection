package com.astriex.reflection.domain.use_case.user_use_case.get_user_data

import androidx.lifecycle.LiveData
import com.astriex.reflection.domain.model.User
import com.astriex.reflection.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(private val repository: FirebaseRepository) {
    operator fun invoke(): LiveData<User> {
        repository.loadUserData()
        return repository.userData
    }
}