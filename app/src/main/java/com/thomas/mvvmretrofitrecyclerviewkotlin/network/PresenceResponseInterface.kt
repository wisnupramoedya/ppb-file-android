package com.thomas.mvvmretrofitrecyclerviewkotlin.network

import com.thomas.mvvmretrofitrecyclerviewkotlin.model.Result
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface PresenceResponseInterface {
    @GET("presences")
    fun getAllPresences() : Call<List<Result>>
    
    @FormUrlEncoded
    @POST("presence")
    fun postPresence(
        @Field("name") name: String,
        @Field("nik") nik: String,
        @Field("image_mask") image_mask: String
    ): Call<Result>
}