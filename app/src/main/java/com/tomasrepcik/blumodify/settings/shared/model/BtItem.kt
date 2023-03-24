package com.tomasrepcik.blumodify.settings.shared.model

import kotlinx.serialization.Serializable

@Serializable
abstract class BtItem(val deviceName: String, val macAddress: String)