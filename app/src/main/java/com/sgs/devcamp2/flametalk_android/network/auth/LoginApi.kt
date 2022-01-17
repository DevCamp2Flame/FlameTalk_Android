package com.sgs.devcamp2.flametalk_android.network.auth

import com.sgs.devcamp2.flametalk_android.data.repository.UserRepository
import com.sgs.devcamp2.flametalk_android.network.request.SignUpRequest
import com.sgs.devcamp2.flametalk_android.network.service.UserService
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginApi @Inject constructor(
    private val userService: Lazy<UserService>,
    private val userRepository: Lazy<UserRepository>,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun signUp(request: SignUpRequest) = withContext(ioDispatcher) {
        userService.get().postSignUp(request)
    }

//    suspend fun signIn(request: SignInRequest): SignInResponse = withContext(ioDispatcher) {
//        userService.get().postSignIn(request)
//            .also { saveUser(it) }
//    }
}