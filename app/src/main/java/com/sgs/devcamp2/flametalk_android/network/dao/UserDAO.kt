package com.sgs.devcamp2.flametalk_android.network.dao

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author 박소연
 * @created 2022/01/19
 * @desc UserDAO(Database Access Object)
 *       내부 DB의 데이터를 가져오는 로직을 처리
 */

@Singleton
class UserDAO @Inject constructor(
    @ApplicationContext val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
){}
