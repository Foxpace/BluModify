package com.tomasrepcik.blumodify.settings.advanced.shared.model

import kotlinx.serialization.Serializable

@Serializable
abstract class BtItem(val deviceName: String, val macAddress: String)