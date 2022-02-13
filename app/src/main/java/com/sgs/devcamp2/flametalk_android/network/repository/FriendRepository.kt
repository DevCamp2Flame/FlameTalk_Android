package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.data.model.friend.FriendModel
import com.sgs.devcamp2.flametalk_android.data.source.local.dao.FriendDAO
import com.sgs.devcamp2.flametalk_android.network.request.friend.AddContactFriendRequest
import com.sgs.devcamp2.flametalk_android.network.request.friend.AddFriendRequest
import com.sgs.devcamp2.flametalk_android.network.request.friend.FriendStatusRequest
import com.sgs.devcamp2.flametalk_android.network.service.FriendService
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 정보와 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class FriendRepository @Inject constructor(
    private val friendService: Lazy<FriendService>,
    private val friendDAO: Lazy<FriendDAO>,
    private val ioDispatcher: CoroutineDispatcher
) {
    // 연락처 친구 추가
    suspend fun postContactFriendAdd(request: AddContactFriendRequest) = withContext(ioDispatcher) {
        friendService.get().postContactFriendAdd(request)
    }

    // 친구 추가
    suspend fun postFriendAdd(request: AddFriendRequest) = withContext(ioDispatcher) {
        friendService.get().postFriendAdd(request)
    }

    // 친구 리스트 조회
    suspend fun getFriendList(isBirthday: Boolean?, isHidden: Boolean?, isBlocked: Boolean?) =
        withContext(ioDispatcher) {
            friendService.get().getFriendList(isBirthday, isHidden, isBlocked)
        }

    // 친구 숨김, 차단여부 변경
    suspend fun putFriendStatus(friendId: Long, request: FriendStatusRequest) =
        withContext(ioDispatcher) {
            friendService.get().putFriendStatus(friendId, request)
        }

    // 친구 리스트 로컬에 저장
    suspend fun insertAllFriends(vararg friends: FriendModel) = withContext(ioDispatcher) {
        friendDAO.get().insertAllFriends(*friends)
    }

    // 친구 리스트 전체 가져오기
    suspend fun getAllFriends() = withContext(ioDispatcher) {
        friendDAO.get().getAllFriends()
    }
}
