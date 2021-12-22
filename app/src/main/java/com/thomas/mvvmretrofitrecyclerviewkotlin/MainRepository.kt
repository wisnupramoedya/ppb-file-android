package com.thomas.mvvmretrofitrecyclerviewkotlin

import com.thomas.mvvmretrofitrecyclerviewkotlin.network.PresenceResponseInterface

class MainRepository constructor(private val presenceResponseInterface: PresenceResponseInterface) {
    fun getAllPresences() = presenceResponseInterface.getAllPresences()
}