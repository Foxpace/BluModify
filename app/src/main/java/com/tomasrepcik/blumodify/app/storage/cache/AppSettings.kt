package com.tomasrepcik.blumodify.app.storage.cache

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val onboarded: Boolean,
    val uuid: String
){

    companion object {
        val default = AppSettings(false, "")
    }
}


