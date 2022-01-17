package com.sgs.devcamp2.flametalk_android.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sgs.devcamp2.flametalk_android.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) {

    private val dataStore = context.dataStore

    private val token: Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }
    private val nickname: Flow<String?> = dataStore.data.map { it[NICKNAME_KEY] }

    val user = combine(token, nickname) { token, nickname ->
        when {
            token == null -> null
            nickname == null -> null

            else -> User(
                token = token,
                nickname = nickname
            )
        }
    }

    private suspend fun setNickname(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(NICKNAME_KEY)
            else it[NICKNAME_KEY] = item
        }
    }

    private suspend fun setToken(item: String?) = withContext(ioDispatcher) {
        dataStore.edit {
            if (item == null) it.remove(TOKEN_KEY)
            else it[TOKEN_KEY] = item
        }
    }

    suspend fun setUser(user: User?) = withContext(ioDispatcher) {
        setNickname(user?.nickname)
        setToken(user?.token)
    }

    suspend fun updateUserNickname(item: String?) = withContext(ioDispatcher) {
        setNickname(item)
    }

    // TODO: 시리얼라이저 구현해야함.
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("user.token")
        private val NICKNAME_KEY = stringPreferencesKey("user.nickname")
    }
}
