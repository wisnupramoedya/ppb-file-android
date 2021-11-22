package com.wisnupram.uploadfile

import com.wisnupram.uploadfile.model.UploadedImageResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UploadImageInterface {
    @FormUrlEncoded
    @POST("img")
    fun postImage(@Field("image") image: String): Call<UploadedImageResponse>
}