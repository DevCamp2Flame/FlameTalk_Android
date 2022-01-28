package com.sgs.devcamp2.flametalk_android.network.service

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

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
        @Part file: MultipartBody.Part,
        @Part chatRoomId: MultipartBody.Part
    )

    @GET("/api/file/{fileId}")
    suspend fun getCreatedFile(
        @Path("fileId") id: Long?
    ): ResponseBody

    @DELETE("/api/file/{fileId}")
    suspend fun deleteCreatedFile(
        @Path("fileId") id: Long?
    ): ResponseBody
}
