package com.thomas.mvvmretrofitrecyclerviewkotlin

import com.thomas.mvvmretrofitrecyclerviewkotlin.network.PresenceResponseInterface

class MainRepository constructor(private val presenceResponseInterface: PresenceResponseInterface) {
    fun getAllPresences() = presenceResponseInterface.getAllPresences()

    fun postPresence(name: String, nik: String, image_mask: String) = presenceResponseInterface.postPresence(name, nik, image_mask)
}