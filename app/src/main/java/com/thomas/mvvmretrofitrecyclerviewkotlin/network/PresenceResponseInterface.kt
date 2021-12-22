package com.thomas.mvvmretrofitrecyclerviewkotlin.network

import com.thomas.mvvmretrofitrecyclerviewkotlin.model.Result
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PresenceResponseInterface {
    @GET("presences")
    fun getAllPresences() : Call<List<Result>>
}