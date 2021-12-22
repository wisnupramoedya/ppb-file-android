package com.wisnupram.uploadfile

class MainRepository constructor(private val presenceResponse: PresenceResponseInterface) {
    fun getAllPresence() = presenceResponse.getPresences()
}