package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.network.request.FileCreateRequest
import com.sgs.devcamp2.flametalk_android.network.service.FileService
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * @author 박소연
 * @created 2022/01/24
 * @updated 2022/02/07
 * @desc 파일과 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class FileRepository @Inject constructor(
    private val fileService: Lazy<FileService>,
    private val ioDispatcher: CoroutineDispatcher,
) {
    //  파일 생성
    suspend fun postFileCreate(body: FileCreateRequest) = withContext(ioDispatcher) {
        fileService.get().postCreateFile(body)
    }

    //  파일 조회
    suspend fun getFile(fileId: Long) = withContext(ioDispatcher) {
        fileService.get().getFile(fileId)
    }

    //  파일 id로 삭제
    suspend fun deleteFileById(fileId: Long) = withContext(ioDispatcher) {
        fileService.get().deleteFileById(fileId)
    }

    //  파일 url로 삭제
    suspend fun deleteFileByUrl(fileUrl: String) = withContext(ioDispatcher) {
        fileService.get().deleteFileByUrl(fileUrl)
    }
}
