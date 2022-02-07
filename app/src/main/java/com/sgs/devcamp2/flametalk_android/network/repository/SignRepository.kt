package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.data.model.User
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.request.sign.SigninRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.SignupRequest
import com.sgs.devcamp2.flametalk_android.network.response.sign.SigninResponse
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
        userDAO.get().removeToken()
        userService.get().postSignin(request).also { saveUser(it) }
    }

    // 로그인 후 유저 정보 저장
    private suspend fun saveUser(response: SigninResponse) = withContext(ioDispatcher) {
        val result = response.data

        if (result.userId == null) return@withContext
        if (result.nickname == null) return@withContext
        if (result.status == null) return@withContext
        if (result.accessToken == null) return@withContext
        if (result.refreshToken == null) return@withContext
        userDAO.get().setUser(
            User(
                result.userId,
                result.nickname,
                result.status,
                result.accessToken,
                result.refreshToken
            )
        )
    }

    // 이메일 중복체크
    suspend fun emailCheck(email: String) = withContext(ioDispatcher) {
        userDAO.get().removeToken()
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
