package com.sgs.devcamp2.flametalk_android.network.dao

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

/**
 * @author 박소연
 * @created 2022/01/13
 * @updated 2022/01/20
 * @desc 사용자의 닉네임과 인증 토큰을 내부 DB에 관리하는 Preference
 */

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

@Singleton
class UserDAO @Inject constructor(
    @ApplicationContext val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) {

    private val dataStore = context.dataStore

    private val nickname: Flow<String?> = dataStore.data.map { it[NICKNAME_KEY] }
    private val accessToken: Flow<String?> = dataStore.data.map { it[ACCESS_TOKEN_KEY] }
    private val refreshToken: Flow<String?> = dataStore.data.map { it[REFRESH_TOKEN_KEY] }

    val user = combine(nickname, accessToken, refreshToken) { nickname, accessToken, refreshToken ->
        when {
            nickname == null -> null
            accessToken == null -> null
            refreshToken == null -> null

            else -> User(
                nickname = nickname,
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }

    private suspend fun setNickname(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(NICKNAME_KEY)
            else it[NICKNAME_KEY] = item
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

    suspend fun setUser(user: User?) = withContext(ioDispatcher) {
        setNickname(user?.nickname)
        setAccessToken(user?.accessToken)
        setAccessToken(user?.refreshToken)
    }

    suspend fun updateUserNickname(item: String?) = withContext(ioDispatcher) {
        setNickname(item)
    }

    // TODO: 시리얼라이저 구현해야함.
    companion object {
        private val NICKNAME_KEY = stringPreferencesKey("user.nickname")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("user.accessToken")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("user.refreshToken")
    }
}
