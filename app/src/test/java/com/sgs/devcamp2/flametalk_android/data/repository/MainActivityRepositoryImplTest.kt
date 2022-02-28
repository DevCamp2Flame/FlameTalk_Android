package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.domain.repository.MainActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.HttpUrl
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.junit.Assert.*
import org.junit.Test
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/28
 */
class MainActivityRepositoryImplTest @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val stompClient: StompClient,
) : MainActivityRepository {


}
