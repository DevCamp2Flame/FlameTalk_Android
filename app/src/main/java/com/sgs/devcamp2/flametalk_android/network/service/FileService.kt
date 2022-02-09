package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.response.CommonResponse
import com.sgs.devcamp2.flametalk_android.network.response.file.FileCreateResponse
import com.sgs.devcamp2.flametalk_android.network.response.file.FileGetResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @author 박소연
 * @created 2022/01/24
 * @updated 2022/02/07
 * @desc 파일 네트워크 통신 인터페이스
 */

interface FileService {
    // 파일 생성
    @Multipart
    @POST("/api/file")
    suspend fun postCreateFile(
        @Part file: MultipartBody.Part,
        @Part("chatroomId") title: RequestBody?,
    ): FileCreateResponse

    // 파일 조회
    @GET("/api/file/{fileId}")
    suspend fun getFile(
        @Path("fileId") fileId: Long
    ): FileGetResponse

    // 파일 id로 삭제
    @DELETE("/api/file/{fileId}")
    suspend fun deleteFileById(
        @Path("fileId") fileId: Long
    ): CommonResponse

    // 파일 url로 삭제
    @DELETE("/api/file")
    suspend fun deleteFileByUrl(
        @Query("fileUrl") fileUrl: String
    ): CommonResponse
}
