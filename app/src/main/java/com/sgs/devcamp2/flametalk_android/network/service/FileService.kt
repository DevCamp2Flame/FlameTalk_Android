package com.sgs.devcamp2.flametalk_android.network.service

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileService {
    @Multipart
    @POST("/api/file")
    suspend fun postCreateFile(
        @Part record: MultipartBody.Part?,
        @Part chatroomId: String?
    )
}
