package com.wisnupram.uploadfile.model

import com.google.gson.annotations.SerializedName

data class UploadedImageResponse (
    @SerializedName("status")
    var status: String?,
    @SerializedName("msg")
    var msg: String? = null
)