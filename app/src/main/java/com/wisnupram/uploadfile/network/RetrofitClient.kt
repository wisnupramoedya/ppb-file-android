package com.wisnupram.uploadfile.network

import com.wisnupram.uploadfile.PresenceResponseInterface
import com.wisnupram.uploadfile.UploadImageInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.2:5000/"

    val instance: UploadImageInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UploadImageInterface::class.java)
    }

    val instance2: PresenceResponseInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PresenceResponseInterface::class.java)
    }
}