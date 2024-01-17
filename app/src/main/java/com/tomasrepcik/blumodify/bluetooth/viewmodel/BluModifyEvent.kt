package com.tomasrepcik.blumodify.bluetooth.viewmodel

sealed class BluModifyEvent {
    data object OnLaunch: BluModifyEvent()
    data object OnMainButtonClickEvent: BluModifyEvent()
    data object OnError: BluModifyEvent()
    data object OnPermissionGranted : BluModifyEvent()
    data object OnPermissionDenied : BluModifyEvent()
}
