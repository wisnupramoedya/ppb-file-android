package com.thomas.mvvmretrofitrecyclerviewkotlin.model

data class Result(
    val created_at: String,
    val id: String,
    val image_mask: String,
    val mask: Int,
    val name: String,
    val nik: String,
)