package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.network.service.FileService
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author 박소연
 * @created 2022/01/24
 * @desc 파일과 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class FileRepository @Inject constructor(
    private val fileService: Lazy<FileService>,
    private val ioDispatcher: CoroutineDispatcher,
) {
    //  파일 생성
    suspend fun postFileCreate(file: MultipartBody.Part, chatRoomId: MultipartBody.Part) = withContext(ioDispatcher) {
        try {
            // fileService.get().postCreateFile(file, chatroomId)
            fileService.get().postCreateFile(file, chatRoomId)
        } catch (e: Exception) {
            e.stackTraceToString()
        }
    }

    //  파일 조회
    suspend fun getCreatedFile(fileId: Long?) = withContext(ioDispatcher) {
        fileService.get().getCreatedFile(fileId)
    }

    //  파일 삭제
    suspend fun deleteCreatedFile(fileId: Long?) = withContext(ioDispatcher) {
        fileService.get().deleteCreatedFile(fileId)
    }
}
