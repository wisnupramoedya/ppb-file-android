package com.wisnupram.uploadfile

import com.wisnupram.uploadfile.model.PresenceResponse
import com.wisnupram.uploadfile.model.Result
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PresenceResponseInterface {
    @GET("presences")
    fun getPresences(): Call<List<Result>>

    companion object {
        var presenceResponse: PresenceResponseInterface? = null

        fun getInstance(): PresenceResponseInterface {
            if (presenceResponse == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.2:5000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                presenceResponse = retrofit.create(PresenceResponseInterface::class.java)
            }

            return presenceResponse!!
        }
    }
}