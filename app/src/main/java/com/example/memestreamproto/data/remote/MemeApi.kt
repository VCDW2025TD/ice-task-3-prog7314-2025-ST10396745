package com.example.memestreamproto.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemeApi {
    @GET("memes")
    suspend fun getMemes(@Query("userId") userId: String? = null): MemeListDto

    @POST("memes")
    suspend fun createMeme(@Body body: MemePost): MemeDto
}
