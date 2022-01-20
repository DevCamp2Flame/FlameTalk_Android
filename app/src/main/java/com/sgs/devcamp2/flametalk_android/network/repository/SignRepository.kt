package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.network.repository.user.UserRepository
import com.sgs.devcamp2.flametalk_android.network.service.UserService
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author 박소연
 * @created 2022/01/19
 * @desc 로그인, 회원가입과 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class SignRepository @Inject constructor(
    private val userService: Lazy<UserService>,
    private val userRepository: Lazy<UserRepository>,
    private val ioDispatcher: CoroutineDispatcher,
) {

//     네트워크 모듈 예시
//    suspend fun signUp(request: SignUpRequest) = withContext(ioDispatcher) {
//        userService.get().postSignUp(request)
//    }
}
