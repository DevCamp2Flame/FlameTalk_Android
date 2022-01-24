package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.network.service.FileService
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

/**
 * @author 박소연
 * @created 2022/01/24
 * @desc 파과 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class FileRepository @Inject constructor(
    private val fileService: Lazy<FileService>,
    private val ioDispatcher: CoroutineDispatcher,
) {

    //     네트워크 모듈 예시
    suspend fun signUp(file: MultipartBody.Part, chatroomId: String?) = withContext(ioDispatcher) {
        fileService.get().postCreateFile(file, chatroomId)
    }
}
