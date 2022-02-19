package com.sgs.devcamp2.flametalk_android.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*

/**
 * @author boris
 * @created 2022/02/19
 */
@HiltWorker
class FcmUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val repository: ChatRoomRepository,
    val db: AppDatabase,
    val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(context, params) {
    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val TAG: String = "로그"
        val text = inputData.getString("text")
        Log.d(TAG, "FcmUpdateWorker - doWork(text : $text) called")
        val roomId = inputData.getString("room")
        Log.d(TAG, "FcmUpdateWorker - doWork(roomId: $roomId) called")
        var flag = 0
        val deffer: Deferred<Int> = CoroutineScope(ioDispatcher).async {
            db.chatRoomDao().updateText(text, roomId, System.currentTimeMillis())
        }
        flag = deffer.await()

        return Result.Success()
    }
}
