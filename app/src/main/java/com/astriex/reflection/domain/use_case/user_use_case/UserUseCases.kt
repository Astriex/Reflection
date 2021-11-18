package com.astriex.reflection.domain.use_case.user_use_case

import com.astriex.reflection.domain.use_case.user_use_case.get_user_data.GetUserDataUseCase
import com.astriex.reflection.domain.use_case.user_use_case.is_user_logged_in.IsUserLoggedInUseCase
import com.astriex.reflection.domain.use_case.user_use_case.login_user.LoginUserUseCase
import com.astriex.reflection.domain.use_case.user_use_case.logout_user.LogoutUserUseCase
import com.astriex.reflection.domain.use_case.user_use_case.register_user.RegisterUserUseCase

data class UserUseCases(
    val registerUserUseCase: RegisterUserUseCase,
    val loginUserUseCase: LoginUserUseCase,
    val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    val getUserDataUseCase: GetUserDataUseCase,
    val logoutUserUseCase: LogoutUserUseCase
)
