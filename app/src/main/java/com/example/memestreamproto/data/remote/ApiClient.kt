package com.example.memestreamproto.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:4000/") // emulator → your Node API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: MemeApi by lazy { retrofit.create(MemeApi::class.java) }
}
