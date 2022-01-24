package com.sgs.devcamp2.flametalk_android.network.service

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * @author 박소연
 * @created 2022/01/24
 * @desc 파일 네트워크 통신 인터페이스
 */

interface FileService {
    // 파일 생성
    @Multipart
    @POST("/api/file")
    suspend fun postCreateFile(
        @Part record: MultipartBody.Part?,
        @Part chatroomId: String?
    )
}
