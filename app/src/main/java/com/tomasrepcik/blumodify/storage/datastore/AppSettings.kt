package com.tomasrepcik.blumodify.storage.datastore

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val onboarded: Boolean
){
    companion object {
        val default = AppSettings(false)
    }
}


