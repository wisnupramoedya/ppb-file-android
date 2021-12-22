package com.thomas.mvvmretrofitrecyclerviewkotlin.model

data class Result(
    val created_at: String,
    val id: String,
    val image_face: String,
    val image_ktp: String,
    val image_mask: String,
    val image_profile: String,
    val mask: Int,
    val name: String,
    val nik: String,
)