package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/31
 */
class ChatRepositoryImp @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val db: AppDatabase,

)
