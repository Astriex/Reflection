package com.astriex.reflection.presentation.activity.intro

import androidx.lifecycle.ViewModel
import com.astriex.reflection.domain.use_case.user_use_case.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(userUseCases: UserUseCases) : ViewModel() {

    var currentUser = userUseCases.isUserLoggedInUseCase()
}