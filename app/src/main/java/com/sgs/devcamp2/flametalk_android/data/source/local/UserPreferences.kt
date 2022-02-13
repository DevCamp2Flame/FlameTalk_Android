package com.sgs.devcamp2.flametalk_android.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sgs.devcamp2.flametalk_android.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/01/13
 * @updated 2022/02/05
 * @desc 사용자의 닉네임과 인증 토큰을 내부 DB에 관리하는 Preference
 */

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) {

    private val dataStore = context.dataStore

    private val userId: Flow<String?> = dataStore.data.map { it[USER_ID_KEY] }
    private val nickname: Flow<String?> = dataStore.data.map { it[NICKNAME_KEY] }
    private val status: Flow<String?> = dataStore.data.map { it[STATUS_KEY] }
    private val accessToken: Flow<String?> = dataStore.data.map { it[ACCESS_TOKEN_KEY] }
    private val refreshToken: Flow<String?> = dataStore.data.map { it[REFRESH_TOKEN_KEY] }

    val user = combine(
        userId,
        nickname,
        status,
        accessToken,
        refreshToken
    ) { userId, nickname, status, accessToken, refreshToken ->
        when {
            userId == null -> null
            nickname == null -> null
            status == null -> null
            accessToken == null -> null
            refreshToken == null -> null

            else -> User(
                userId = userId,
                nickname = nickname,
                status = status,
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }

    private suspend fun setUserId(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(USER_ID_KEY)
            else it[USER_ID_KEY] = item
        }
    }

    private suspend fun setNickname(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(NICKNAME_KEY)
            else it[NICKNAME_KEY] = item
        }
    }

    private suspend fun setStatus(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(STATUS_KEY)
            else it[STATUS_KEY] = item
        }
    }

    private suspend fun setAccessToken(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(ACCESS_TOKEN_KEY)
            else it[ACCESS_TOKEN_KEY] = item
        }
    }

    private suspend fun setRefreshToken(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(REFRESH_TOKEN_KEY)
            else it[REFRESH_TOKEN_KEY] = item
        }
    }

    // 유저 생성
    suspend fun setUser(user: User?) = withContext(ioDispatcher) {
        setUserId(user?.userId)
        setNickname(user?.nickname)
        setStatus(user?.status)
        setAccessToken(user?.accessToken)
        setRefreshToken(user?.refreshToken)
        Timber.d("유저 생성 성공 $user")
    }

    // 토큰 갱신
    suspend fun renewUserToken(accessToken: String, refreshToken: String) =
        withContext(ioDispatcher) {
            setAccessToken(accessToken)
            setRefreshToken(refreshToken)
        }

    suspend fun logoutUser() = withContext(ioDispatcher) {
        setUserId(null)
        setNickname(null)
        setStatus(null)
        setAccessToken(null)
        setRefreshToken(null)
    }

    suspend fun removeToken() = withContext(ioDispatcher) {
        setAccessToken(null)
    }

    fun getAccessToken(): Flow<String?> {
        return this.accessToken
    }

    fun getRefreshToken(): Flow<String?> {
        return this.refreshToken
    }

    // TODO: 시리얼라이저 구현해야함.
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user.userId")
        private val NICKNAME_KEY = stringPreferencesKey("user.nickname")
        private val STATUS_KEY = stringPreferencesKey("user.status")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("user.accessToken")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("user.refreshToken")
    }
}
