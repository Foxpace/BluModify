package com.tomasrepcik.blumodify.bluetooth.viewmodel

sealed class BluModifyEvent {
    object OnLaunch: BluModifyEvent()
    object OnMainButtonClickEvent: BluModifyEvent()
    object OnError: BluModifyEvent()
    object OnPermissionGranted : BluModifyEvent()
}
