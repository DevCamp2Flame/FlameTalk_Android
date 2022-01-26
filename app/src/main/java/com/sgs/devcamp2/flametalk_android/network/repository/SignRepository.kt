package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.request.sign.SigninRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.SignupRequest
import com.sgs.devcamp2.flametalk_android.network.service.UserService
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * @author 박소연
 * @created 2022/01/19
 * @desc 로그인, 회원가입과 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class SignRepository @Inject constructor(
    private val userService: Lazy<UserService>,
    private val userDAO: Lazy<UserDAO>,
    private val ioDispatcher: CoroutineDispatcher
) {
    // 회원가입
    suspend fun signup(request: SignupRequest) = withContext(ioDispatcher) {
        userService.get().postSignup(request)
    }

    // 로그인
    suspend fun signin(request: SigninRequest) = withContext(ioDispatcher) {
        userService.get().postSignin(request)
    }

    // 이메일 중복체크
    suspend fun emailCheck(email: String) = withContext(ioDispatcher) {
        userService.get().getEmailCheck(email)
    }

    // 탈퇴
    suspend fun leaveUser() = withContext(ioDispatcher) {
        userService.get().deleteLeaveUser()
            .also {
                if (it.status == 200)
                    userDAO.get().setUser(null)
            }
    }
}
