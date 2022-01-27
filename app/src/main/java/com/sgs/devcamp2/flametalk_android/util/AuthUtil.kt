package com.sgs.devcamp2.flametalk_android.util

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.service.AuthService
import kotlinx.coroutines.runBlocking

/**
 * @author 박소연
 * @created 2022/01/27
 * @updated 2022/01/27
 * @desc 토큰 갱신을 위한 단독의 통신 요청 로직
 */
class AuthUtil {
    // 로그아웃 시 기기에 저장된 사용자 정보를 삭제한다.
    fun logout(activity: Activity, userDAO: UserDAO) {
        runBlocking {
            userDAO.logoutUser()
        }
        Snackbar.make(activity.currentFocus!!, "로그아웃 되었습니다.", Snackbar.LENGTH_SHORT).show()
    }

    fun postRefreshToken(userService: AuthService, userDAO: UserDAO) {
        runBlocking {
            val response = userService.getRenewToken()
            userDAO.renewUserToken(response.data.accessToken, response.data.refreshToken)
        }
    }
}
