package com.thomas.mvvmretrofitrecyclerviewkotlin.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://152609208610.ip-dynamic.com/"

    val instance: PresenceResponseInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PresenceResponseInterface::class.java)
    }
}