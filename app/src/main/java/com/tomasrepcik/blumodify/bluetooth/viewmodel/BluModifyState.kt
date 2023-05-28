package com.tomasrepcik.blumodify.bluetooth.viewmodel

import com.tomasrepcik.blumodify.app.model.AppResult

sealed class BluModifyState {
    object Loading: BluModifyState()
    object TurnedOff: BluModifyState()
    object TurnedOn: BluModifyState()
    object MissingPermission: BluModifyState()
    data class ErrorOccurred(val error: AppResult.Error<Boolean>): BluModifyState()
}
