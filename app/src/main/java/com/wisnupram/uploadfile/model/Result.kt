package com.wisnupram.uploadfile.model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("id")
    val id: String,
    @SerializedName("image_profile")
    val image_profile: String,
    @SerializedName("mask")
    val mask: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("nik")
    val nik: String
)