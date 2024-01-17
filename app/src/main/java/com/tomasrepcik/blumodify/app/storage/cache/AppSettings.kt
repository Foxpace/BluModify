package com.tomasrepcik.blumodify.app.storage.cache

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val isOnboarded: Boolean = false,
    val isAdvancedSettings: Boolean = false
) {

    companion object {
        val default = AppSettings(isOnboarded = false, isAdvancedSettings = false)
    }
}


